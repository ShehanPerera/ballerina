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
package org.ballerinalang.observe.metrics.extension.prometheus.provider;

import org.ballerinalang.util.metrics.AbstractMetric;
import org.ballerinalang.util.metrics.Counter;

/**
 * {@link Counter} implementation wrapping {@link io.prometheus.client.Counter}.
 */
public class PrometheusCounter extends AbstractMetric<Counter> implements Counter {

    private final Counter.Builder builder;
    private final io.prometheus.client.Counter counter;

    public PrometheusCounter(Counter.Builder builder) {
        super(builder);
        this.builder = builder;
        counter = io.prometheus.client.Counter.build(builder.getName(), builder.getDescription()).create();
    }

    @Override
    protected Counter createNewChild() {
        return new PrometheusCounter(builder);
    }

    @Override
    public void increment(double amount) {
        counter.inc(amount);
    }

    @Override
    public double count() {
        return counter.get();
    }
}
