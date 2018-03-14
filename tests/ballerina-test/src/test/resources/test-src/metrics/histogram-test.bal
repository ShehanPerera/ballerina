import ballerina.metrics;

metrics:Histogram histogram = {name:"requests_latency_seconds", help:"Request latency in seconds.", namespace:"sample",
                                  subsystem:"worker_pool", labelNames:["method"], buckets:[.01, .02, .03, .04]};

function testRegisterHistogram() {
    histogram.register();
}

function testObserveHistogram() {
    histogram.observe(21.5f);
}

function testCreateLinearBuckets() (float[]){
    return histogram.createLinearBuckets(4.2f, 2.3f, 2);
}

function testCreateExponentialBuckets() (float[]){
    return histogram.createExponentialBuckets(3.5f, 1.4f, 3);
}

function testGetHistogram() (metrics:HistogramValue){
    return histogram.get();
}
