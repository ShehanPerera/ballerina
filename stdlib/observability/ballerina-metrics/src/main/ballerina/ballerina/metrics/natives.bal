// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package ballerina.metrics;

@Description {value:"Counter metric, to track counts of events or running totals."}
@Field {value:"name: The name of the counter. Required."}
@Field {value:"help: The help string of the metric. Required."}
@Field {value:"namespace: The namespace of the metric. Optional."}
@Field {value:"subsystem: The subsystem of the metric. Optional."}
@Field {value:"labelNames: The labels to be used with metric. Optional."}
public struct Counter {
    string name;
    string help;
    string namespace;
    string subsystem;
    string[] labelNames;
}

@Description {value:"Create and register the counter."}
@Param {value:"counter: The counter instance to be registered."}
public native function <Counter counter> register();

@Description {value:"Increment the counter by one."}
@Param {value:"counter: The counter instance to be incremented by one."}
@Param {value:"labelValues: values of the lableNames defined in the Counter."}
public native function <Counter counter> incrementByOne(string[] labelValues);

@Description {value:"Increment the counter by the given amount."}
@Param {value:"counter: The counter instance to be incremented by 'amount'."}
@Param {value:"amount: float to be added with the counter value."}
@Param {value:"labelValues: values of the lableNames defined in the Counter."}
public native function <Counter counter> increment(float amount, string[] labelValues);

@Description {value:"Get the value of the counter."}
@Param {value:"counter: The counter instance to be returned."}
@Return {value:"The value of a counter"}
public native function <Counter counter> get() (float);

@Description {value:"Gauge metric, to report instantaneous values. Gauges can go both up and down."}
@Field {value:"name: The name of the gauge. Required."}
@Field {value:"help: The help string of the metric. Required."}
@Field {value:"namespace: The namespace of the metric. Optional."}
@Field {value:"subsystem: The subsystem of the metric. Optional."}
@Field {value:"labelNames: The labels to be used with metric. Optional."}
public struct Gauge {
    string name;
    string help;
    string namespace;
    string subsystem;
    string[] labelNames;
}

@Description {value:"Create and register the gauge."}
@Param {value:"gauge: The gauge instance to be registered."}
public native function <Gauge gauge> register();

@Description {value:"Increment the gauge by one."}
@Param {value:"gauge: The gauge instance to be incremented by one."}
@Param {value:"labelValues: values of the lableNames defined in the gauge."}
public native function <Gauge gauge> incrementByOne(string[] labelValues);

@Description {value:"Increment the gauge by the given amount."}
@Param {value:"gauge: The gauge instance to be incremented by 'amount'."}
@Param {value:"amount: float to be added with the gauge value."}
@Param {value:"labelValues: values of the lableNames defined in the gauge."}
public native function <Gauge gauge> increment(float amount, string[] labelValues);

@Description {value:"Decrement the gauge by one."}
@Param {value:"gauge: The gauge instance to be decremented by one."}
@Param {value:"labelValues: values of the lableNames defined in the gauge."}
public native function <Gauge gauge> decrementByOne(string[] labelValues);

@Description {value:"Decrement the gauge by the given amount."}
@Param {value:"gauge: The gauge instance to be decremented by 'amount'."}
@Param {value:"amount: float to be added with the gauge value."}
@Param {value:"labelValues: values of the lableNames defined in the gauge."}
public native function <Gauge gauge> decrement(float amount, string[] labelValues);

@Description {value:"Set the gauge to the given value."}
@Param {value:"gauge: The gauge instance to be set."}
@Param {value:"value: value to be set to the gauge."}
public native function <Gauge gauge> setGauge(float value);

@Description {value:"Set the gauge to the current unix time."}
@Param {value:"gauge: The gauge instance to be set."}
public native function <Gauge gauge> setToCurrentTime();

@Description {value:"Get the value of the gauge."}
@Param {value:"gauge: The gauge instance to be returned."}
@Return {value:"The value of a gauge"}
public native function <Gauge gauge> get() (float);

@Description {value:"Histogram metric, to track distributions of events."}
@Field {value:"name: The name of the histogram. Required."}
@Field {value:"help: The help string of the metric. Required."}
@Field {value:"namespace: The namespace of the metric. Optional."}
@Field {value:"subsystem: The subsystem of the metric. Optional."}
@Field {value:"labelNames: The labels to be used with metric. Optional."}
@Field {value:"buckets: The upper bounds of buckets for the histogram."}
public struct Histogram {
    string name;
    string help;
    string namespace;
    string subsystem;
    string[] labelNames;
    float[] buckets;
}

@Description {value:"Create and register the histogram."}
@Param {value:"histogram: The histogram instance to be registered."}
public native function <Histogram histogram> register();

@Description {value:"Observe the given amount."}
@Param {value:"histogram: The histogram instance to be observed."}
@Param {value:"amount: float value to be observed."}
public native function <Histogram histogram> observe(float amount);

@Description {value:"Histogram Value"}
@Field {value:"sum: The total sum of all events in buckets."}
@Field {value:"buckets: The buckets with events count."}
public struct HistogramValue {
    float sum;
    float[] buckets;
}

@Description {value: "Create the upper bounds of buckets for the histogram with a linear sequence."}
@Param {value: "histogram: The histogram instance."}
@Param {value: "start: "}
@Param {value: "width: "}
@Param {value: "count: "}
@Return {value: ""}
public native function <Histogram histogram> createLinearBuckets(float start, float width, int count) (float[]);

@Description {value: "Create the upper bounds of buckets for the histogram with an exponential sequence."}
@Param {value: "histogram: The histogram instance."}
@Param {value: "start: "}
@Param {value: "factor: "}
@Param {value: "count: "}
@Return {value: ""}
public native function <Histogram histogram> createExponentialBuckets(float start, float factor, int count) (float[]);

@Description {value: "Get the current values in Histogram"}
@Param {value: "histogram: "}
@Return {value: ""}
public native function <Histogram histogram> get() (HistogramValue);

@Description {value:"Summary metric, to track the size of events."}
@Field {value:"name: The name of the summary. Required."}
@Field {value:"help: The help string of the metric. Required."}
@Field {value:"namespace: The namespace of the metric. Optional."}
@Field {value:"subsystem: The subsystem of the metric. Optional."}
@Field {value:"labelNames: The labels to be used with metric. Optional."}
@Field {value:"quantiles: An array of arrays with quantile and error percentage."}
@Field {value: "maxAgeSeconds: The duration of the time window."}
@Field {value: "ageBuckets: The number of buckets used to implement the sliding time window."}
public struct Summary {
    string name;
    string help;
    string namespace;
    string subsystem;
    string[] labelNames;
    float[][] quantiles;
    int maxAgeSeconds;
    int ageBuckets;
}

@Description {value:"Create and register the summary."}
@Param {value:"summary: The summary instance to be registered."}
public native function <Summary summary> register();

@Description {value:"Observe the given amount."}
@Param {value:"summary: The summary instance to be observed."}
@Param {value:"amount: float value to be observed."}
public native function <Summary summary> observe(float amount);

@Description {value:"Summary Value."}
@Field {value:"count: The total number of events."}
@Field {value:"sum: The total sum of all observed events."}
@Field {value:"quantiles: All quantiles with values."}
public struct SummaryValue {
    float count;
    float sum;
    float[][] quantiles;
}

@Description {value: "Get the current values in Summary"}
@Param {value: "summary: "}
@Return {value: ""}
public native function <Summary summary> get() (SummaryValue );
