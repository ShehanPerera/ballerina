import ballerina.metrics;

metrics:Counter counter = {name:"requests_total",help:"Total requests.", namespace:"sample", subsystem:"worker_pool",
                              labelNames:["method"]};

//metrics:Counter counter = {name:"requests_total",help:"Total requests.", labelNames:["method"]};

function testRegisterCounter() {
    counter.register();
}

function testCounterIncrementByOne() {
    counter.incrementByOne(["get"]);
}

function tetsCounterIncrement() {
    counter.increment(5.0f, ["get"]);
}

function testGetCounter() (float){
    return counter.get();
}
