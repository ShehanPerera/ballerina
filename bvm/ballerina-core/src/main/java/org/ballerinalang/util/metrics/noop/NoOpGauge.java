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
package org.ballerinalang.util.metrics.noop;

import org.ballerinalang.util.metrics.AbstractMetric;
import org.ballerinalang.util.metrics.Gauge;
import org.ballerinalang.util.metrics.MetricId;

/**
 * Implementation of No-Op {@link Gauge}.
 */
public class NoOpGauge extends AbstractMetric<Gauge> implements Gauge {

    public NoOpGauge(MetricId id) {
        super(id);
        this.builder = builder;
    }

    @Override
    protected Gauge createNewChild() {
        return new NoOpGauge(builder);
    }

    @Override
    public void increment(double amount) {
        // Do nothing
    }

    @Override
    public void decrement(double amount) {
        // Do nothing
    }

    @Override
    public void set(double value) {
        // Do nothing
    }

    @Override
    public double get() {
        return 0;
    }
}
