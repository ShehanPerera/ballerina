import ballerina.metrics;

metrics:Summary summary = {name:"requests_size_bytes", help:"Request size in bytes.", namespace:"sample",
                              subsystem:"worker_pool", labelNames:["method"], quantiles:[[2.3f,4.2f],[1.2f,6.7f]],
                              maxAgeSeconds:5, ageBuckets:4};

function testRegisterSummary() {
    summary.register();
}

function testObserveSummary() {
    summary.observe(3.2f);
}

function testGetSummary() (metrics:SummaryValue) {
    return summary.get();
}