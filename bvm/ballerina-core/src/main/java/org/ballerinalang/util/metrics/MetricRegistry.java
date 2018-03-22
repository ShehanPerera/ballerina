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

import org.ballerinalang.util.metrics.noop.NoOpMetricProvider;
import org.ballerinalang.util.metrics.spi.MetricExtension;
import org.ballerinalang.util.metrics.spi.MetricProvider;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Registry for keeping metrics by name.
 */
public class MetricRegistry {

    private final MetricProvider metricProvider;
    private final ConcurrentMap<String, Metric> metrics;

    /**
     * Lazy initialization for Default {@link MetricRegistry}. Initialize all {@link MetricExtension}s.
     */
    private static class LazyHolder {
        static final MetricRegistry REGISTRY = new MetricRegistry();

        static {
            // Load extensions
            ServiceLoader<MetricExtension> extensionServiceLoader = ServiceLoader.load(MetricExtension.class);
            extensionServiceLoader.forEach(metricExtension -> metricExtension.initialize());
        }
    }

    public static MetricRegistry getDefaultRegistry() {
        return LazyHolder.REGISTRY;
    }

    public MetricRegistry() {
        this(() -> {
            // Look for MetricProvider implementations
            Iterator<MetricProvider> metricProviders = ServiceLoader.load(MetricProvider.class).iterator();
            MetricProvider metricProvider = null;
            while (metricProviders.hasNext()) {
                MetricProvider temp = metricProviders.next();
                if (!NoOpMetricProvider.class.isInstance(temp)) {
                    metricProvider = temp;
                }
            }
            if (metricProvider == null) {
                metricProvider = new NoOpMetricProvider();
            }
            return metricProvider;
        });
    }

    public MetricRegistry(Supplier<MetricProvider> metricProviderSupplier) {
        this(metricProviderSupplier.get());
    }

    public MetricRegistry(MetricProvider metricProvider) {
        this.metrics = new ConcurrentHashMap<>();
        this.metricProvider = metricProvider;
    }

    private <M extends Metric> M getMetric(Class<M> metricClass, String name) throws MetricNotFoundException {
        Metric metric = metrics.get(name);
        if (metric != null && metricClass.isInstance(metric)) {
            return (M) metric;
        }
        throw new MetricNotFoundException(metricClass.getSimpleName() + " not found for name: " + name);
    }

    public Counter getCounter(String name) throws MetricNotFoundException {
        return getMetric(Counter.class, name);
    }

    public Gauge getGauge(String name) throws MetricNotFoundException {
        return getMetric(Gauge.class, name);
    }

    Counter counter(Counter.Builder builder) {
        return getOrCreate(builder.getName(), Counter.class, () -> metricProvider.newCounter(builder));
    }

    Gauge gauge(Gauge.Builder builder) {
        return getOrCreate(builder.getName(), Gauge.class, () -> metricProvider.newGauge(builder));
    }

    private <M extends Metric> M getOrCreate(String name, Class<M> metricClass, Supplier<M> metricSupplier) {
        final Metric metric = metrics.get(name);
        if (metric != null) {
            if (metricClass.isInstance(metric)) {
                return (M) metric;
            }
        }
        synchronized (metrics) {
            Metric<M> newMetric = metricSupplier.get();
            final Metric<M> existing = metrics.putIfAbsent(name, newMetric);
            if (existing != null) {
                if (metricClass.isInstance(metric)) {
                    return (M) existing;
                } else {
                    throw new IllegalArgumentException(name + " is already used for a different type of metric");
                }
            }
            return (M) newMetric;
        }
    }

    /**
     * Removes the metric with the given name.
     *
     * @param name the name of the metric
     * @return whether or not the metric was removed
     */
    public boolean remove(String name) {
        final Metric metric = metrics.remove(name);
        return metric != null;
    }
}
