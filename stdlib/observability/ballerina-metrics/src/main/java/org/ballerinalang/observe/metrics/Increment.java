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
package org.ballerinalang.observe.metrics;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;

/**
 * TODO: Class level comment.
 */
@BallerinaFunction(
        packageName = "ballerina.metrics",
        functionName = "increment",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "Counter",
                structPackage = "ballerina.metrics"),
        args = {@Argument(name = "label", type = TypeKind.STRING),
                @Argument(name = "n", type = TypeKind.INT)},
        isPublic = true
)
public class Increment extends AbstractNativeFunction{
    @Override
    public BValue[] execute(Context context) {
        BStruct counter = (BStruct) getRefArgument(context, 0);
        String name = counter.getStringField(0);
        String help = counter.getStringField(1);
//        String labelNames = counter.getStringField(2);
        BStringArray labelNames = (BStringArray) counter.getRefField(0);
        BStringArray labelValues = (BStringArray) getRefArgument(context,1);
        int n = (int) getIntArgument(context,0);
        //need to call the Prometheus wrapper methods
        return VOID_RETURN;
    }
}
