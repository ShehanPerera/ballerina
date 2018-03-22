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
 * Gauge is used to track a value that goes up and down. Gauge can also report instantaneous values.
 */
public interface Gauge extends Metric<Gauge> {

    static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link Gauge}s.
     */
    class Builder extends Metric.Builder<Builder, Gauge> {
        @Override
        public Gauge register(MetricRegistry registry) {
            return registry.gauge(this);
        }
    }

    /**
     * Increment the gauge by one.
     */
    default void increment() {
        increment(1D);
    }

    /**
     * Increment the gauge by {@code amount}.
     *
     * @param amount Amount to add to the gauge.
     */
    void increment(double amount);

    /**
     * Decrement the gauge by one.
     */
    default void decrement() {
        decrement(1D);
    }

    /**
     * Decrement the gauge by {@code amount}.
     *
     * @param amount Amount to subtract from the gauge.
     */
    void decrement(double amount);

    /**
     * Set the gauge to the given value.
     */
    void set(double value);

    /**
     * @return The value of the gauge.
     */
    double get();

}
