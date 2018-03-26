package org.ballerinalang.observe.metrics.extension.prometheus.provider;

import org.ballerinalang.util.metrics.Counter;
import org.ballerinalang.util.metrics.Gauge;
import org.ballerinalang.util.metrics.MetricRegistry;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PrometheusMetricProviderTest {

    private MetricRegistry metricRegistry;

    @BeforeClass
    public void init() {
        metricRegistry = new MetricRegistry(new PrometheusMetricProvider());
    }

    @Test
    public void testCounter() {
        Counter counter = Counter.builder().name("test_counter").description("Test Counter").register(metricRegistry);
        counter.increment(100D);
        Assert.assertEquals(counter.count(), 100D);
    }

    @Test
    public void testCounterTags() {
        Counter counter = Counter.builder().name("test_tags_counter").description("Test Counter Tags")
                .tagKeys("key").register(metricRegistry);
        counter.increment();
        Map<String, String> tags = new HashMap<>();
        tags.put("key", "value");
        counter.tags(tags).increment(100D);
        counter.tags("value").increment(100D);
        Counter counterWithTags = counter.tags(tags);
        counterWithTags.increment(100D);
        Assert.assertEquals(counterWithTags.count(), 300D);
    }

    @Test
    public void testCounterTagsFailures() {
        Counter counter = Counter.builder().name("test_tag_fail_counter").description("Test Counter Tags Failures")
                .tagKeys("key").register(metricRegistry);
        Map<String, String> tags = new HashMap<>();
        tags.put("key", "value");
        counter.tags(tags).increment(100D);
        try {
            counter.tags("value", "value2").increment(100D);
            Assert.fail("Different number of tag values are not allowed");
        } catch (IllegalArgumentException e) {
            // Test successful
        }
        counter.tags("value").increment(100D);
        Counter counterWithTags = counter.tags(tags);
        counterWithTags.increment(100D);
        Assert.assertEquals(counterWithTags.count(), 300D);
    }

    @Test
    public void testGauge() {
        Gauge gauge = Gauge.builder().name("test_gauge").description("Test Gauge").register(metricRegistry);
        gauge.increment(100D);
        gauge.decrement(50D);
        Assert.assertEquals(gauge.get(), 50D);
    }
}
