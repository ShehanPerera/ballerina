package org.ballerinalang.observe.metrics.extension.prometheus.httpserver;

import io.prometheus.client.Collector;

import java.util.List;
import java.util.stream.Stream;

public class PrometheusCollector extends Collector {

    private final String name;
    private final String help;
    private final Type type;
    private final List<String> tagKeys;

    interface Child {
        Stream<MetricFamilySamples.Sample> samples(String conventionName, List<String> tagKeys);
    }

    public PrometheusCollector(String name, String help, Type type, List<String> tagKeys) {
        this.name = name;
        this.help = help;
        this.type = type;
        this.tagKeys = tagKeys;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        return null;
    }
}
