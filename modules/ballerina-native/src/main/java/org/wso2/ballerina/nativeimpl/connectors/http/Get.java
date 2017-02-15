/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.ballerina.nativeimpl.connectors.http;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.ballerina.core.exception.BallerinaException;
import org.wso2.ballerina.core.interpreter.Context;
import org.wso2.ballerina.core.model.Connector;
import org.wso2.ballerina.core.model.types.TypeEnum;
import org.wso2.ballerina.core.model.values.BConnector;
import org.wso2.ballerina.core.model.values.BMessage;
import org.wso2.ballerina.core.model.values.BValue;
import org.wso2.ballerina.core.nativeimpl.annotations.Argument;
import org.wso2.ballerina.core.nativeimpl.annotations.Attribute;
import org.wso2.ballerina.core.nativeimpl.annotations.BallerinaAction;
import org.wso2.ballerina.core.nativeimpl.annotations.BallerinaAnnotation;
import org.wso2.ballerina.core.nativeimpl.annotations.ReturnType;
import org.wso2.ballerina.core.nativeimpl.connectors.AbstractNativeAction;
import org.wso2.carbon.messaging.CarbonMessage;

/**
 * {@code Get} is the GET action implementation of the HTTP Connector.
 */
@BallerinaAction(
        packageName = "ballerina.net.http",
        actionName = "get",
        connectorName = ClientConnector.CONNECTOR_NAME,
        args = {
                @Argument(name = "con",
                        type = TypeEnum.CONNECTOR),
                @Argument(name = "path", type = TypeEnum.STRING),
                @Argument(name = "message", type = TypeEnum.MESSAGE)
        },
        returnType = {@ReturnType(type = TypeEnum.MESSAGE)})
@Component(
        name = "action.net.http.get",
        immediate = true,
        service = AbstractNativeAction.class)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
value = "GET action implementation of the HTTP Connector") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "connector",
value = "Connector") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "path",
value = "Request path") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "message",
value = "message") })
public class Get extends AbstractHTTPAction {

    private static final Logger logger = LoggerFactory.getLogger(Get.class);

    @Override
    public BValue execute(Context context) {

        logger.debug("Executing Native Action : Get");

        try {
            // Extract Argument values
            BConnector bConnector = (BConnector) getArgument(context, 0);
            String path = getArgument(context, 1).stringValue();
            BMessage bMessage = (BMessage) getArgument(context, 2);

            Connector connector = bConnector.value();
            if (!(connector instanceof ClientConnector)) {
                throw new BallerinaException("Need to use a ClientConnector as the first argument", context);
            }
            // Prepare the message
            CarbonMessage cMsg = bMessage.value();
            prepareRequest(connector, path, cMsg);
            cMsg.setProperty(Constants.HTTP_METHOD,
                             Constants.HTTP_METHOD_GET);

            // Execute the operation
            return executeAction(context, cMsg);
        } catch (Throwable t) {
            throw new BallerinaException("Failed to invoke 'get' action in " + ClientConnector.CONNECTOR_NAME
                                         + ". " + t.getMessage(), context);
        }
    }
}
