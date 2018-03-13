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
package org.ballerinalang.observe.metrics.counter;

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
 * This function return the value of the counter.
 */
@BallerinaFunction(
        packageName = "ballerina.metrics",
        functionName = "get",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "Counter",
                structPackage = "ballerina.metrics"),
        returnType = {@ReturnType(type = TypeKind.FLOAT)},
        isPublic = true
)
public class GetCounter extends AbstractNativeFunction {

    @Override
    public BValue[] execute(Context context) {
        BStruct counter = (BStruct) getRefArgument(context, 0);
        String name = counter.getStringField(0);
        String help = counter.getStringField(1);
        String namespace = counter.getStringField(2);
        String subsystem = counter.getStringField(3);
        BStringArray labelNames = (BStringArray) counter.getRefField(0);
        float count = 20.0f;
        return getBValues(new BFloat(count));
    }
}
