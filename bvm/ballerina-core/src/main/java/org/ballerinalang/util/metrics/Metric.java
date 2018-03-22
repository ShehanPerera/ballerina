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

import java.util.List;
import java.util.Map;

/**
 * Main interface for metrics.
 *
 * @param <M> Metric class.
 */
public interface Metric<M> {

    /**
     * Add tags for this metric.
     *
     * @param tags A map of key value pairs to be used as tags
     * @return Metric instance
     */
    M tags(Map<String, String> tags);

    /**
     * Add tags for this metric.
     *
     * @param tags A list of key value pairs to be used as tags
     * @return Metric instance
     */
    M tags(List<Tag> tags);

    /**
     * Add tags for this metric.
     *
     * @param keyValues Key value pairs to be used as tags
     * @return Metric instance
     */
    M tags(String... keyValues);

    /**
     * Builder for metrics.
     *
     * @param <B> Builder for a given metric class.
     * @param <M> Metric class.
     */
    abstract class Builder<B extends Builder<B, M>, M extends Metric> {

        // Name is used to uniquely identify metrics
        private String name = "";

        // Description of the metric
        private String description = "";

        /**
         * Set the name of the metric. Required.
         */
        public B name(String name) {
            this.name = name;
            return (B) this;
        }

        /**
         * Set the description of the metric. Required.
         */
        public B description(String description) {
            this.description = description;
            return (B) this;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        /**
         * Create and register the metric with the default registry.
         *
         * @return Registered metric
         */
        public M register() {
            return register(MetricRegistry.getDefaultRegistry());
        }

        /**
         * Create and register the metric with the given registry.
         *
         * @param registry {@link MetricRegistry} to be used
         * @return Registered metric
         */
        public abstract M register(MetricRegistry registry);
    }

}
