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
package org.ballerinalang.util.metrics;

/**
 * A monotonically increasing counter metric. Use {@link Counter} Gauge to track a value that goes up and down.
 */
public interface Counter extends Metric<Counter> {

    static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link Counter}s.
     */
    class Builder extends Metric.Builder<Builder, Counter> {
        @Override
        public Counter register(MetricRegistry registry) {
            return registry.counter(this);
        }
    }

    /**
     * Increment the counter by one.
     */
    default void increment() {
        increment(1D);
    }

    /**
     * Increment the counter by {@code amount}.
     *
     * @param amount Amount to add to the counter.
     */
    void increment(double amount);

    /**
     * @return The cumulative count since this counter was created.
     */
    double count();

}
