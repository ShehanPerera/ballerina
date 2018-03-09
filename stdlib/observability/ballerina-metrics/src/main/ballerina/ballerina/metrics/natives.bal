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

@Description {value:"Counter is a metric collection which is used to count an event."}
@Field {value:"Identifier of the counter."}
public struct Counter {
    string name;
    string help;
    string[] labelNames;
}

@Description {value:"Increment the counter value by one."}
@Param {value:"counter: The counter instance to be incremented by one."}
@Param {value:"labelValues: values of the lableNames defined in the Counter."}
public native function <Counter counter> incrementByOne(string[] labelValues);

@Description {value:"Increment the counter value by n."}
@Param {value:"counter: The counter instance to be incremented by n."}
@Param {value:"n: integer value to be added with the counter value."}
@Param {value:"labelValues: values of the lableNames defined in the Counter."}
public native function <Counter counter> increment(string[] labelValues, int n);
