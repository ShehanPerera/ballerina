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
import org.ballerinalang.model.values.BFloatArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests for Histogram metric.
 */
public class HistogramTest {
    private CompileResult compileResult;

    @BeforeTest
    public void setup() {
        compileResult = BCompileUtil.compile("test-src/metrics/histogram-test.bal");
    }

    @Test (priority = 1)
    public void testRegisterHistogram() {
        BRunUtil.invoke(compileResult, "testRegisterHistogram", new BValue[]{});
    }

    @Test (priority = 2)
    public void testObserveHistogram() {
        BRunUtil.invoke(compileResult, "testObserveHistogram", new BValue[]{});
    }

    @Test (priority = 3)
    public void testCreateLinearBuckets() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testCreateLinearBuckets", new BValue[]{});

        Assert.assertEquals(returns[0].stringValue(), new BFloatArray(new double[]{2.3, 6.7, 3.5}).stringValue(),
                "Invalid bucket float array returned.");
    }

    @Test (priority = 4)
    public void testCreateExponentialBuckets() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testCreateExponentialBuckets", new BValue[]{});
        Assert.assertEquals(returns[0].stringValue(), new BFloatArray(new double[]{2.3, 6.7, 3.5}).stringValue(),
                "Invalid bucket float array returned.");

    }

    @Test (priority = 5)
    public void testGetHistogram() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testGetHistogram", new BValue[]{});
        Assert.assertEquals(((BStruct) returns[0]).getFloatField(0), 34.5, "Invalid sum float returned.");
        Assert.assertEquals(((BStruct) returns[0]).getRefField(0).stringValue(),
                new BFloatArray(new double[]{.01, .02, .03, .04}).stringValue(),
                "Invalid buckets float array returned.");
    }
}
