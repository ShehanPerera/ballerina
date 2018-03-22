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

import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.util.metrics.Counter;
import org.ballerinalang.util.metrics.Gauge;
import org.ballerinalang.util.metrics.spi.MetricProvider;

/**
 * {@link MetricProvider} implementation to provide Prometheus Metric Implementations.
 */
@JavaSPIService("org.ballerinalang.util.metrics.spi.MetricProvider")
public class PrometheusMetricProvider implements MetricProvider {

    @Override
    public Counter newCounter(Counter.Builder builder) {
        return new PrometheusCounter(builder);
    }

    @Override
    public Gauge newGauge(Gauge.Builder builder) {
        return new PrometheusGauge(builder);
    }
}
