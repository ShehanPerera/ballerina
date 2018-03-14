/*
 *
 *   Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 * /
 */
package org.ballerinalang.observe.metrics.summary;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BLangVMStructs;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BRefValueArray;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.util.codegen.PackageInfo;
import org.ballerinalang.util.codegen.StructInfo;

/**
 * This function return the current values in Summary.
 */
@BallerinaFunction(
        packageName = "ballerina.metrics",
        functionName = "get",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "Summary",
                structPackage = "ballerina.metrics"),
        returnType = {@ReturnType(type = TypeKind.STRUCT, structType = "SummaryValue",
                structPackage = "ballerina.metrics")},
        isPublic = true
)
public class GetSummary extends AbstractNativeFunction {
    private static final String SUMMARY_VALUE_PACKAGE = "ballerina.metrics";
    private static final String SUMMARY_VALUE_STRUCT_TYPE = "SummaryValue";

    @Override
    public BValue[] execute(Context context) {
        BStruct summary = (BStruct) getRefArgument(context, 0);
        String name = summary.getStringField(0);
        String help = summary.getStringField(1);
        String namespace = summary.getStringField(2);
        String subsystem = summary.getStringField(3);
        BStringArray labelNames = (BStringArray) summary.getRefField(0);
        BRefValueArray quantiles = (BRefValueArray) summary.getRefField(1);
        int maxAgeSeconds = (int) summary.getIntField(0);
        int ageBuckets = (int) summary.getIntField(1);

        PackageInfo packageInfo = context.getProgramFile().getPackageInfo(SUMMARY_VALUE_PACKAGE);
        StructInfo structInfo = packageInfo.getStructInfo(SUMMARY_VALUE_STRUCT_TYPE);
        BStruct summaryValue = BLangVMStructs.createBStruct(structInfo);
        summaryValue.setFloatField(0, 34.5f);
        summaryValue.setFloatField(1, 43.5f);
        summaryValue.setRefField(0, quantiles);

        return getBValues(summaryValue);
    }
}
