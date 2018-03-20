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
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;

/**
 * This function increment the counter by one.
 */

@BallerinaFunction(
        packageName = "ballerina.metrics",
        functionName = "incrementByOne",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "Counter",
                structPackage = "ballerina.metrics"),
        args = {@Argument(name = "labelValues", type = TypeKind.STRING)},
        isPublic = true
)
public class IncrementCounterByOne extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        BStruct counter = (BStruct) context.getRefArgument(0);
        String name = counter.getStringField(0);
        String help = counter.getStringField(1);
        String namespace = counter.getStringField(2);
        String subsystem = counter.getStringField(3);
        BStringArray labelNames = (BStringArray) counter.getRefField(0);
        BStringArray labelValues = (BStringArray) context.getRefArgument(1);
    }
}
