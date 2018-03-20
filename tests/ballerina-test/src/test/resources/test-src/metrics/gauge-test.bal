import ballerina.metrics;

metrics:Gauge gauge = {name:"inprogress_requests",help:"Inprogress requests.", namespace:"sample",
                          subsystem:"worker_pool", labelNames:["method"]};

function testRegisterGauge() {
    gauge.register();
}

function testIncrementGaugeByOne() {
    gauge.incrementByOne(["get"]);
}

function testIncrementGauge() {
    gauge.increment(5.0f, ["get"]);
}

function testDecrementGaugeByOne() {
    gauge.decrementByOne(["get"]);
}

function testDecrementgauge() {
    gauge.decrement(2.3f, ["get"]);
}

function testSetGauge() {
    gauge.setGauge(12.4f);
}

function testSetGaugeToCurrentTime() {
    gauge.setToCurrentTime();
}

function testGetGauge() (float){
    return gauge.get();
}