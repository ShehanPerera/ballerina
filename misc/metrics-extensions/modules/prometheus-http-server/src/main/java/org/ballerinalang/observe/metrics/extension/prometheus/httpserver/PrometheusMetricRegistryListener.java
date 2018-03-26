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
package org.ballerinalang.observe.metrics.extension.prometheus.httpserver;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.util.metrics.Counter;
import org.ballerinalang.util.metrics.Gauge;
import org.ballerinalang.util.metrics.spi.MetricProvider;
import org.ballerinalang.util.metrics.spi.MetricRegistryListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * {@link MetricProvider} implementation to provide Prometheus Metric Implementations.
 */
@JavaSPIService("org.ballerinalang.util.metrics.spi.MetricRegistryListener")
public class PrometheusMetricRegistryListener implements MetricRegistryListener {

    private final ConcurrentMap<String, PrometheusCollector> collectorMap = new ConcurrentHashMap<>();
    private final CollectorRegistry registry;

    public PrometheusMetricRegistryListener() {
        this(CollectorRegistry.defaultRegistry);
    }

    public PrometheusMetricRegistryListener(CollectorRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void initialize() {
        // Start prometheus server
        try {
            new HTTPServer(8688, true);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private PrometheusCollector getOrCreateCollector(String name, Collector.Type type) {
        return collectorMap.compute(name, (metricName, existingCollector) -> {
            List<String> tagKeys
            if (existingCollector == null) {
                return new PrometheusCollector(name, type).register(registry);
            }

            List<String> tagKeys = getConventionTags(id).stream().map(Tag::getKey).collect(toList());

            if (existingCollector.getTagKeys().equals(tagKeys)) {
                return existingCollector;
            }

            throw new IllegalArgumentException("Prometheus requires that all meters with the same name have the same" +
                    " set of tag keys. There is already an existing meter containing tag keys [" +
                    existingCollector.getTagKeys().stream().collect(joining(", ")) + "]. The meter you are attempting to register" +
                    " has keys [" + tagKeys.stream().collect(joining(", ")) + "].");
        });
    }

    @Override
    public void onCounterAdded(String name, Counter counter) {
        counter.
    }

    @Override
    public void onCounterRemoved(String name) {

    }

    @Override
    public void onGaugeAdded(String name, Gauge gauge) {

    }

    @Override
    public void onGaugeRemoved(String name) {

    }
}
