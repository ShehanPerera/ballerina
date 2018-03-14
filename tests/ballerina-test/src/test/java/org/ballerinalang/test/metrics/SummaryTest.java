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
package org.ballerinalang.test.metrics;

import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BRunUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests for Summary metric.
 */
public class SummaryTest {
    private CompileResult compileResult;

    @BeforeTest
    public void setup() {
        compileResult = BCompileUtil.compile("test-src/metrics/summary-test.bal");
    }

    @Test (priority = 1)
    public void testRegisterSummary() {
        BRunUtil.invoke(compileResult, "testRegisterSummary", new BValue[]{});
    }

    @Test (priority = 2)
    public void testObserveSummary() {
        BRunUtil.invoke(compileResult, "testObserveSummary", new BValue[]{});
    }

    @Test (priority = 3)
    public void testGetSummary() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testGetSummary", new BValue[]{});
        Assert.assertEquals(((BStruct) returns[0]).getFloatField(0), 34.5, "Invalid count float returned.");
        Assert.assertEquals(((BStruct) returns[0]).getFloatField(1), 43.5, "Invalid sum float returned.");
        Assert.assertEquals(((BStruct) returns[0]).getRefField(0).stringValue(),
                "[[2.3, 4.2], [1.2, 6.7]]", "Invalid quantiles float[][] returned.");
    }
}
