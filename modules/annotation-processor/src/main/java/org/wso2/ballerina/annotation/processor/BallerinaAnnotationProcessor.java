/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.ballerina.annotation.processor;

import org.wso2.ballerina.annotation.processor.holders.ActionHolder;
import org.wso2.ballerina.annotation.processor.holders.ConnectorHolder;
import org.wso2.ballerina.annotation.processor.holders.FunctionHolder;
import org.wso2.ballerina.annotation.processor.holders.PackageHolder;
import org.wso2.ballerina.annotation.processor.holders.TypeConvertorHolder;
import org.wso2.ballerina.core.exception.BallerinaException;
import org.wso2.ballerina.core.nativeimpl.annotations.BallerinaAction;
import org.wso2.ballerina.core.nativeimpl.annotations.BallerinaAnnotation;
import org.wso2.ballerina.core.nativeimpl.annotations.BallerinaConnector;
import org.wso2.ballerina.core.nativeimpl.annotations.BallerinaFunction;
import org.wso2.ballerina.core.nativeimpl.annotations.BallerinaTypeConvertor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Read all class annotations of native functions, connectors, actions, type converters.
 * Process them and generate a service provider class that will register all the annotated 
 * classes as {@link Symbol}s to the global symbol table, via java SPI.
 */
@SupportedAnnotationTypes({ "org.wso2.ballerina.core.nativeimpl.annotations.BallerinaFunction",
                            "org.wso2.ballerina.core.nativeimpl.annotations.BallerinaConnector",
                            "org.wso2.ballerina.core.nativeimpl.annotations.BallerinaAction",
                            "org.wso2.ballerina.core.nativeimpl.annotations.BallerinaTypeConvertor"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({ "className", "packageName", "targetDir" })
public class BallerinaAnnotationProcessor extends AbstractProcessor {

    private static final String CLASS_NAME = "className";
    private static final String PACKAGE_NAME = "packageName";
    private static final String TARGET_DIR = "targetDir";
    private Map<String, PackageHolder> nativePackages;
    
    public BallerinaAnnotationProcessor() throws IOException {
        super();
        this.nativePackages = new HashMap<String, PackageHolder>();
    }
    
    /**
     * Process the ballerina annotations and generate the construct provider class.
     * <br/>
     * {@inheritDoc}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<Element> balFunctionElements = (Set<Element>) roundEnv.getElementsAnnotatedWith(BallerinaFunction.class);
        Set<Element> balConnectorElements = (Set<Element>) roundEnv.getElementsAnnotatedWith(BallerinaConnector.class);
        Set<Element> balActionElements = (Set<Element>) roundEnv.getElementsAnnotatedWith(BallerinaAction.class);
        Set<Element> balTypeConvertorElements =
                (Set<Element>) roundEnv.getElementsAnnotatedWith(BallerinaTypeConvertor.class);
        
        // If all annotations are empty, should not do anything. Continue to the next plugin phases.
        if (balFunctionElements.isEmpty() && balConnectorElements.isEmpty() && balActionElements.isEmpty()
                    && balTypeConvertorElements.isEmpty()) {
            return true;
        }
        
        // Process all native function, connectors, actions and type converters
        processNativeFunctions(balFunctionElements);
        processNativeConnectors(balConnectorElements);
        processNativeActions(balActionElements);
        processNativeTypeConvertors(balTypeConvertorElements);
        
        Map<String, String> options = processingEnv.getOptions();
        
        String targetDir = options.get(TARGET_DIR);
        if (targetDir == null) {
            throw new BallerinaException("target directory to store the generated ballerina files, must be specified.");
        }
        
        // Generate the native ballerina files
        generateNativeBalFiles(targetDir);
        
        // Generate the construct provider class. This should be invoked after 
        // generating the native ballerina files
        generateConstructProviderClass(options, targetDir);
        
        return true;
    }
    
    /**
     * Generate the construct provider class.
     * 
     * @param options Annotation processor options
     * @param targetDir 
     */
    private void generateConstructProviderClass(Map<String, String> options, String targetDir) {
        Filer filer = processingEnv.getFiler();
        
        String classClassName = options.get(CLASS_NAME);
        if (classClassName == null) {
            throw new BallerinaException("class name for the generated construct-provider must be specified.");
        }
        
        String packageName = options.get(PACKAGE_NAME);
        if (packageName == null) {
            throw new BallerinaException("package name for the generated construct-provider must be specified.");
        }
        
        ConstructProviderClassBuilder constructProviderClassBuilder =
                new ConstructProviderClassBuilder(filer, packageName, classClassName, targetDir);
        constructProviderClassBuilder.addNativePackages(nativePackages);
        constructProviderClassBuilder.build();
    }
    
    /**
     * Generate the built-in ballerina files
     * 
     * @param targetDir target directory to generate the built-in ballerina files
     */
    private void generateNativeBalFiles(String targetDir) {
        NativeBallerinaFileBuilder nativeBallerinaFileBuilder = new NativeBallerinaFileBuilder(targetDir);
        nativeBallerinaFileBuilder.addNativePackages(nativePackages);
        nativeBallerinaFileBuilder.build();
    }

    /**
     * Process all {@link BallerinaFunction} annotations and append constructs to the class builder.
     * 
     * @param balFunctionElements Elements annotated with {@link BallerinaFunction}
     */
    private void processNativeFunctions(Set<Element> balFunctionElements) {
        for (Element element : balFunctionElements) {
            BallerinaFunction balFunction = element.getAnnotation(BallerinaFunction.class);
            BallerinaAnnotation[] annot = getBallerinaAnnotations(element);
            String packageName = balFunction.packageName();
            String className = Utils.getClassName(element);
            FunctionHolder function = new FunctionHolder(balFunction, className, annot);
            getPackage(packageName).addFunction(function);
        }
    }

    private BallerinaAnnotation[] getBallerinaAnnotations(Element element) {
        BallerinaAnnotation[] annot = new BallerinaAnnotation[0];
        if (element.getAnnotationsByType(BallerinaAnnotation.class).length > 0) {
            annot = element.getAnnotationsByType(BallerinaAnnotation.class);
        }
        return annot;
    }
    
    /**
     * Process all {@link BallerinaConnector} annotations and append constructs to the class builder.
     * 
     * @param balConnectorElements Elements annotated with {@link BallerinaConnector}
     */
    private void processNativeConnectors(Set<Element> balConnectorElements) {
        for (Element element : balConnectorElements) {
            BallerinaConnector balConnector = element.getAnnotation(BallerinaConnector.class);
            String packageName = balConnector.packageName();
            String className = Utils.getClassName(element);
            ConnectorHolder connector = new ConnectorHolder(balConnector, className);
            connector.setAnnotations(getBallerinaAnnotations(element));
            getPackage(packageName).addConnector(balConnector.connectorName(), connector);
        }
    }
    
    /**
     * Process all {@link BallerinaAction} annotations and append constructs to the class builder.
     * 
     * @param balActionElements Elements annotated with {@link BallerinaAction}
     */
    private void processNativeActions(Set<Element> balActionElements) {
        for (Element element : balActionElements) {
            BallerinaAction balAction = element.getAnnotation(BallerinaAction.class);
            String packageName = balAction.packageName();
            String className = Utils.getClassName(element);
            ActionHolder action = new ActionHolder(balAction, className);
            action.setAnnotations(getBallerinaAnnotations(element));
            getPackage(packageName).getConnector(balAction.connectorName()).addAction(action);
        }
    }
    
    /**
     * Process all {@link BallerinaTypeConvertor} annotations and append constructs to the class builder.
     * 
     * @param balTypeConvertorElements Elements annotated with {@link BallerinaTypeConvertor}
     * @param classBuilder Builder to generate the source class
     * @param nativeBallerinaFileBuilder Builder to generate the native ballerina files
     */
    private void processNativeTypeConvertors(Set<Element> balTypeConvertorElements) {
        for (Element element : balTypeConvertorElements) {
            BallerinaTypeConvertor balTypeConvertor = element.getAnnotation(BallerinaTypeConvertor.class);
            BallerinaAnnotation[] annot = getBallerinaAnnotations(element);
            String packageName = balTypeConvertor.packageName();
            String className = Utils.getClassName(element);
            TypeConvertorHolder typeConvertor = new TypeConvertorHolder(balTypeConvertor, className, annot);
            getPackage(packageName).addTypeConvertor(typeConvertor);
        }
    }
    
    /**
     * Get a package holder by name, from the package map. If a package does not exists with the provided name,
     * this method will create a new package and return it.
     * 
     * @param packageName Name of the package to be retrieved
     * @return Package holder with the provided name
     */
    private PackageHolder getPackage(String packageName) {
        if (nativePackages.containsKey(packageName)) {
            return nativePackages.get(packageName);
        }
        PackageHolder pkg = new PackageHolder(packageName);
        nativePackages.put(packageName, pkg);
        return pkg;
    }
}
