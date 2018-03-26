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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Main interface for metrics.
 *
 * @param <M> Metric class.
 */
public interface Metric<M> {

    /**
     * @return A unique combination of name and tags
     */
    MetricId getId();

    /**
     * Get tag key names used in the metric.
     *
     * @return A list of tag key names
     */
    List<String> getTagKeys();

    /**
     * Add tags for this metric. Tag keys must match the key defined in the metric
     *
     * @param tags A map of key value pairs to be used as tags
     * @return Metric instance
     */
    M tags(Map<String, String> tags);

    /**
     * Add tag values for this metric.
     *
     * @param tagValues A list of key value pairs to be used as tags
     * @return Metric instance
     */
    M tags(List<String> tagValues);

    /**
     * Add tag values for this metric.
     *
     * @param tagValues Tag values to be used for the corresponding tag keys in the metric
     * @return Metric instance
     */
    M tags(String... tagValues);

    /**
     * Builder for metrics.
     *
     * @param <B> Builder for a given metric class.
     * @param <M> Metric class.
     */
    abstract class Builder<B extends Builder<B, M>, M extends Metric> {

        // Name and tags are used to uniquely identify metrics
        private String name;
        private List<Tag> tags;
        // Description of the metric
        private String description;

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

//        /**
//         * Set the tag keys. Optional.
//         */
//        public B tagKeys(String... tagKeys) {
//            this.tagKeys = tagKeys;
//            return (B) this;
//        }


        @Override
        public B tags(Map<String, String> tags) {
            List<String> tagValues = new ArrayList<>(tags.size());
            for (String key : tagKeys) {
                tagValues.add(tags.get(key));
            }
            return  tags(tagValues);
        }

        @Override
        public B tags(List<String> tagValues) {
            return tags(tagValues.toArray(new String[tagValues.size()]));
        }

        @Override
        public B tags(String... tagValues) {
            if (child) {
                throw new IllegalStateException("Cannot add tags to a child metric");
            }
            // The tag values count must match the number of tag keys defined when building metric
            if (tagKeys.size() != tagValues.length) {
                throw new IllegalArgumentException("Incorrect number of tag values");
            }
            for (String value : tagValues) {
                if (value == null) {
                    throw new IllegalArgumentException("Tag value cannot be null.");
                }
            }
            List<String> key = Arrays.asList(tagValues);
            return children.compute(key, (keys, child) -> {
                if (child == null) {
                    child = createNewChild();
                    ((AbstractMetric) child).child = true;
                }
                return child;
            });
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String[] getTagKeys() {
            return tagKeys != null ? Arrays.copyOf(tagKeys, tagKeys.length) : null;
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
