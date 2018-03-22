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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract Metric class implementing tag support with child metrics.
 *
 * @param <M> Metric class.
 */
public abstract class AbstractMetric<M> implements Metric<M> {

    protected final String name;
    protected final String description;

    protected final ConcurrentMap<List<Tag>, M> children = new ConcurrentHashMap<>();

    // Flag to indicate whether this is child metric
    private boolean child;

    public AbstractMetric(Metric.Builder builder) {
        String name = builder.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("Name hasn't been set.");
        }
        String description = builder.getDescription();
        if (description == null || description.isEmpty()) {
            throw new IllegalStateException("Description hasn't been set.");
        }
        this.name = name;
        this.description = description;
    }

    protected abstract M createNewChild();

    @Override
    public M tags(List<Tag> tags) {
        M metric = children.get(tags);
        if (metric == null) {
            metric = createNewChild();
            children.put(tags, metric);
        }
        ((AbstractMetric) metric).child = true;
        return metric;
    }

    @Override
    public M tags(Map<String, String> tags) {
        if (child) {
            throw new IllegalStateException("Cannot add tags to a child metric");
        }
        List<Tag> tagList = new ArrayList<>(tags.size());
        for (Map.Entry<String, String> t : tags.entrySet()) {
            Tag tag = Tag.of(t.getKey(), t.getValue());
            tagList.add(tag);
        }
        return tags(tagList);
    }

    @Override
    public M tags(String... keyValues) {
        if (keyValues == null || keyValues.length == 0) {
            return (M) this;
        }
        if (keyValues.length % 2 == 1) {
            throw new IllegalArgumentException("size must be even, it is a set of key=value pairs");
        }
        List<Tag> tags = new ArrayList<>(keyValues.length / 2);
        for (int i = 0; i < keyValues.length; i += 2) {
            tags.add(Tag.of(keyValues[i], keyValues[i + 1]));
        }
        return tags(tags);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractMetric<?> that = (AbstractMetric<?>) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(name);
    }
}
