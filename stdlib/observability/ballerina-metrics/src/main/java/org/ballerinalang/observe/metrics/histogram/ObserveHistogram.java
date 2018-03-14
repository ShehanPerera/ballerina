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
package org.ballerinalang.observe.metrics.histogram;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BFloatArray;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;

/**
 * This function observe the given amount.
 */
@BallerinaFunction(
        packageName = "ballerina.metrics",
        functionName = "observe",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "Histogram",
                structPackage = "ballerina.metrics"),
        args = {@Argument(name = "amount", type = TypeKind.FLOAT)},
        isPublic = true
)
public class ObserveHistogram extends AbstractNativeFunction {
    @Override
    public BValue[] execute(Context context) {
        BStruct histogram = (BStruct) getRefArgument(context, 0);
        String name = histogram.getStringField(0);
        String help = histogram.getStringField(1);
        String namespace = histogram.getStringField(2);
        String subsystem = histogram.getStringField(3);
        BStringArray labelNames = (BStringArray) histogram.getRefField(0);
        BFloatArray buckets = (BFloatArray) histogram.getRefField(1);
        float amount = (float) getFloatArgument(context, 0);

        return VOID_RETURN;
    }
}
