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
import org.ballerinalang.util.metrics.Gauge;

/**
 * {@link Gauge} implementation wrapping {@link io.prometheus.client.Gauge}.
 */
public class PrometheusGauge extends AbstractMetric<Gauge> implements Gauge {

    private final Gauge.Builder builder;
    private final io.prometheus.client.Gauge gauge;

    public PrometheusGauge(Gauge.Builder builder) {
        super(builder);
        this.builder = builder;
        gauge = io.prometheus.client.Gauge.build(builder.getName(), builder.getDescription()).create();
    }

    @Override
    protected Gauge createNewChild() {
        return new PrometheusGauge(builder);
    }

    @Override
    public void increment(double amount) {
        gauge.inc(amount);
    }

    @Override
    public void decrement(double amount) {
        gauge.dec(amount);
    }

    @Override
    public void set(double value) {
        gauge.set(value);
    }

    @Override
    public double get() {
        return gauge.get();
    }
}
