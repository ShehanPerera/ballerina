import ballerina.metrics;

function testCounterIncrementByOne() {
    metrics:Counter counter = {name:"requests_total",help:"Total requests.",labelNames:["method"]};
    counter.incrementByOne(["get"]);
}

function tetsCounterIncrement() {
    metrics:Counter counter = {name:"requests_total",help:"Total requests.",labelNames:["method"]};
    counter.increment(["post"], 5);
}
