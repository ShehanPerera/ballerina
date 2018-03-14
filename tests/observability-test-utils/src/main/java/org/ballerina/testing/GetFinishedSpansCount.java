/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerina.testing;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;

/**
 * This function returns the span context of a given span.
 */
@BallerinaFunction(
        packageName = "ballerina.testing",
        functionName = "getFinishedSpansCount",
        returnType = {@ReturnType(type = TypeKind.INT)},
        isPublic = true
)
public class GetFinishedSpansCount extends BlockingNativeCallableUnit {

    @Override
    public void execute(Context context) {
//        Map<String, MockTracer> mockTracer = BMockTracer.getTracerMap();
//        final int[] count = {0};
//        mockTracer.forEach((tracerName, tracer) -> count[0] += tracer.finishedSpans().size());
//        return getBValues(new BInteger(count[0]));
    }
}
