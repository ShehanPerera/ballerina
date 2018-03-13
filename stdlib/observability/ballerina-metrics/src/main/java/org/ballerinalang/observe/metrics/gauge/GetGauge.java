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
package org.ballerinalang.observe.metrics.gauge;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BFloat;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.natives.annotations.ReturnType;

/**
 * This function return the value of the gauge.
 */
@BallerinaFunction(
        packageName = "ballerina.metrics",
        functionName = "get",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "Gauge",
                structPackage = "ballerina.metrics"),
        returnType = {@ReturnType(type = TypeKind.FLOAT)},
        isPublic = true
)
public class GetGauge extends AbstractNativeFunction {
    @Override
    public BValue[] execute(Context context) {
        BStruct gauge = (BStruct) getRefArgument(context, 0);
        String name = gauge.getStringField(0);
        String help = gauge.getStringField(1);
        String namespace = gauge.getStringField(2);
        String subsystem = gauge.getStringField(3);
        BStringArray labelNames = (BStringArray) gauge.getRefField(0);
        float count = 20.0f;
        return getBValues(new BFloat(count));
    }
}
