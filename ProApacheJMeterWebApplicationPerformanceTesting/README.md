# Pro Apache JMeter: Web Application Performance Testing

Main notes taken from the book [Pro Apache JMeter: Web Application Performance Testing](https://a.co/d/0EgsrgJ)

## Performance Testing

Performance testing is the testing performed on a system or application to measure some of its attributes such as
response time, throughput, scalability, etc. These attributes are called the performance criteria.

### Response Time

Some terms related to response time include the following:

- Absolute Response Time: This is the total time taken from the instant a user clicks on a link or submits a form until
the response from the server is rendered completely.
- Perceived Response Time: The absolute response time for some complex web pages may be unacceptably high. To solve this
issue, the request/response is structured into a set of request/responses that could be rendered progressively. This
allows the users to continue reading or filling up a form, creating the perception that the response was fast even
though the absolute response time is roughly the same. Perceived response time is the response time as perceived by the
users.
- Server Processing Time: This is the time taken by the server to process the given input and generate the output. This
can vary depending on the complexity of the request, server hardware, and the system load.
- Rendering Time: This is the time taken by the browser to parse and render the response received from the server. This
depends on the complexity of the web page, presence of multimedia, presence of JavaScript, and whether the browser
needs to generate content. It also depends on the system load on the user’s device.
- Network Latency: This is the round-trip time taken by a data packet to travel over the network the server and back.
This does not include the server processing time or the rendering time. This can vary widely depending on the location
of the user, time of the day, and the network load.

### Throughput

A sequence of request/responses constitutes a transaction. The number of transactions per unit time is called the
throughput.

### Utilization

Utilization is the ratio of the throughput of the application relative to its maximum capacity. This is a measure of
how well the application is being used. It is not desirable to operate above 80% utilization because the user requests
do not arrive evenly, and the system should be able to handle a spike in the load.

### Robustness

This is a measure of how well the application detects and handles various errors and exceptions. Mean Time between
Failures (MTbF) is a metric that is often used for this purpose.

### Scalability

Scalability measures how well the system can expand its capacity when additional resources are added. Ideally the
system capacity will increase linearly as additional resources are added.

_Vertical scalability_ is achieved by upgrading the hardware. For example, by adding more memory, disk, a better CPU,
or additional CPUs.

_Horizontal scalability_ is achieved by adding servers to the cluster. For example, by adding more web
servers and application servers to a webfarm/cluster.

## Types of Performance Tests

### Stress Tests

A stress test is a kind of performance test that tests the application beyond the normal limits. The application is
subjected to excess load and after that its stability and performance are noted. This type of test is used to determine
how the application responds to load spikes.

### Load Tests

A load test is a kind of performance test that's performed at the specified load level. So ideally, we would like to
perform load tests at varying load levels to note the behavior of the application.

### Peak Load Tests

A peak load test is performed at the load that the application is expected to handle. For example, e-commerce web sites
experience their peak traffic during Black Friday, Cyber Monday, and the Christmas holidays. So a peak load test in
this case would test the application within the load specification but at the higher end.

### Soak Tests or Endurance Tests

In a soak test (also called an endurance test), the application is subjected to a specified load that is within the
specified limit but for a long duration. It is performed for many hours at a time. This test determines if the
application is properly reusing its resources.

### Scalability Tests

Successful web applications experience massive and sometimes exponential growth. So it is wise to measure how the
application scales. Scalability is defined as how well the application handles the increase in load while still meeting
the desired performance criteria.

### Capacity Tests

A capacity test is a load test that establishes the maximum load that the application can handle while meeting the
desired performance criteria. The resulting metric is called the maximum capacity. It is used in scaling the application
and to estimate costs for future growth.

### Spike Tests and Burst Capacity

A spike test is a load test where the application is subjected to brief periods of sudden increment in load, a small
fraction beyond the maximum capacity. It is usually done to estimate the weakness/strength of an application. The
application is expected to be robust and continue to meet the performance criteria during the spike. This metric is
called the burst capacity.

### Performance Smoke Tests

In a performance smoke test, a few common and essential use-cases along with use-cases pertaining to the code subject
to change are together tested for performance. It is only when the smoke test succeeds that the full suite of
performance tests are conducted. If the smoke test fails, no further performance tests are conducted until the
performance defect has been rectified.

### High Availability Tests/Fail-Over Tests

Modern web application infrastructure is designed to be highly available and resilient to hardware and software
failures. Ideally, the architecture should ensure that there is no single point of failure and that there are standby
servers that can transparently take over without impacting the user experience. In this test various equipment and
software failures are simulated and relevant performance tests are run to verify that the application is still meeting
the performance criteria.

## The Performance Test Environment

The performance environment should be modeled after the production environment. In an ideal scenario, the performance
environment should be a replica of the production environment in terms of hardware, software, network, components, and
topology.

### The Performance Environment Should Be Isolated

The performance environment should be on a different subnet, isolated from the production environment so that any
activity on the performance subnet will not influence production and vice versa.

### Performance Testing Tools

Performance testing tools should address load generation, performance data collection, and analysis and reporting.

## The Performance Testing Strategy

### Performance Requirements

There may be many performance criteria that are used in the industry. While all of them could be useful, only a subset
is applicable to every specific application. Further, an even smaller number may be of critical importance due to
contractual obligations, Service Level Agreements (SLAs), or the application owner’s stipulations. The criteria thus
identified are called performance requirements.

Sometimes the nature of the business dictates these requirements. For example, for a stock trading application, a
sub-second response time is a requirement. Every release has to meet these requirements.

### Performance Goals

Performance goals are criteria that are desired but not critical. These are often determined by the performance of
similar applications from competitors. Meeting these goals would be to the advantage of the business.

### Performance Test Suite

The performance test suite is a set of tests that measures the performance requirements and goals. The company's
performance testing strategy may call for multiple performance test suites. Not all the test suites need to be executed
each time. For example, a test suite for scalability would be run in preparation of busy times such as Christmas, while
a smoke test suite is executed as a part of the build process.

### Performance Reporting and Analysis

Performance reports are analyzed to detect instances where performance requirements are not being met; these are logged
as critical defects.

### Performance Tuning

The engineering team reviews and modifies the application to address defects and concerns raised by the performance
reports. These tuning changes may include modifications to configuration, code, network, architecture, topology, etc.

## Components of a JMeter Test

A JMeter test script consists of hierarchical and ordered components organized in the form of a tree.
Each of these components has properties that can be configured in the following ways:
- By jmeter.properties file
- By using command-line parameters
- By editing the .jmx file directly using a text editor
- By using the GUI
- By using values extracted from the responses received to sampler requests

### Test Plan

The test plan is the top-most element and is the root of the tree. For a test plan, the name, description, and user
variables can be configured.

### Thread Group

Every test has one or more thread groups. A thread group is a child element of a test plan. Each thread group
represents a use-case.

### Controller

Each thread group has one or more controller elements. Logical controllers decide the flow of execution. They determine
the next Sampler to execute. JMeter comes with many built-in logical controllers that provide precise control flow. For
example, the If controller and Switch controller provide branching; the `ForEach`, `While`, and `For` provide iteration
flow. There is a controller for every programming construct. A custom controller, if needed, could be developed using
the plugin API mechanism provided by JMeter.

### Sampler
Sampler is a child element of a thread group or a controller. It sends requests to the server. For every protocol, we
need a separate sampler. Out of the box, JMeter comes with many samplers. For example, to send a HTTP request, you use
a HTTP Request Sampler. Custom samplers can be developed using the JMeter plugin mechanism.

### Listener

Listeners listen to the responses from the server and assemble and aggregate the performance metrics. They are used to
display graphs. We need at least one listener per test script so that we can interpret and understand the results of
the performance test.

### Timer

A timer introduces a delay in the flow. Delay is needed between sampler requests for the following reasons:

- To simulate the time that the user takes to perform the next action on the web page
- To simulate a realistic load distribution on the server
- 
Add timers as child elements of samplers or logic controller that need the delay.

### Assertions

Assertions are used to verify that the server responses are as expected. Assertions test various status codes, and then
pause, alert, or log bad request/responses.

Add an Assertion Results Listener to the thread group to view the assertion results. Failed assertions will also be
displayed in the View Results Tree and the View Results in Table Listeners, and will count toward the error percentage.

### Config Element

Configuration elements are placeholders for properties, including user-defined properties and variables. For example,
the HTTP Cookie Manager is a configuration element. Configuration elements can be scoped out with a nesting level.

### Pre-Processors

Pre-processors take the request and modify it (substitution, enhancement, de-referencing variables, etc.) before the
sampler sends it to the server.

### Post-Processors

Post-processors process the response from the server. They are used to process the server response and modify the
component settings or to update variables.

### Order of Component Execution

In a Thread Group the following components are executed in this order:

Config Element(s) -> Pre-Processor(s) -> Timer(s) -> Logic Controller(s)/Sampler(s) -> Post-Processor(s) -> Assertion(s)
 -> Listener(s)

### GUI/Non-GUI Mode

Use the GUI to create and debug the tests scripts. Use the non-GUI mode to execute the test and collect the results.

To run a test from the non-gui mode, use: `jmeter -n -t <path_to_test_plan_file> -l <log_file_name>`
 
Example:

```shell
jmeter -n -t TestPlanFile.jmx -l TestRunResults.jtl
```

### JMeter in Server Mode

To run JMeter in server mode, use: `jmeter-server`

## JMeter WorkBench

(Not available in recent versions - the "Test plan" serves as workbench)

JMeter's WorkBench provides a temporary workspace to store test elements, including a thread group. When the JMeter
GUI starts, it is pre-populated with an empty test plan and an empty WorkBench. When JMeter is configured as a proxy,
it can record the browser activity in the WorkBench. Users can then copy/paste recorded requests from the WorkBench
into the test plan.

## JMeter Test Plan Components

### Test Plan

The test plan is the root element of the JMeter test. It is a container that holds JMeter components like the
thread group, logic controller, sampler, listener, timer, assertion, and config element.

### Thread Group

The Thread Group element is the starting point of execution. All elements can be the child elements of a test plan or
a thread group except for controllers and samplers, which can only be the child elements of a thread group.

The thread group simulates the load generated by users performing a use-case. A test plan can have multiple thread
groups, thus simulating multiple use-cases.

#### Thread Properties

- Number of Threads (Users): This is the number of users we want to use for load testing a web application.
- Ramp-Up Period (in Seconds): This is the time after which all threads will be active.

Example: Start 10 threads, with a thread starting every second; then the configuration must be:

- Number of Threads (users): 10
- RampUp Period (in Seconds) as 10.

With this configuration, 10 threads will start in 10 seconds and all threads will be active after 10 seconds.

### Pre-Processors

Pre-processors take the request and modify (substitution, enhancement, de-referencing variables etc.) it before the
sampler sends it to the server.

Pre-processors are executed after the Config Elements and before the timers and samplers as per JMeter test plan
execution order.

#### HTTP URL Re-Writing Modifier

- Session Argument Name: Variable used to store the session ID in the web application.
- Path Extension (Use ";" as Separator): Should be selected only when the URL parameters are separated by
semicolons (;).
- Do not use equals in path extension: Should be selected if you do not want name/value pair separated by equals
signs (=).
- Do not use question mark in path extension: Should be selected if you do not want name/value pair separated by
question marks (?)
- Cache Session Id?: Should be selected if you want to use the Caching feature of JMeter.
- URL Encode: Should be selected if your URL is using any encoding.

### Controller

Controllers determine the sequence in which the samplers are processed.

#### Simple Controller

The Simple Controller provides no functionality beyond that of grouping, primarily to organize samplers and other
logic controllers.

#### Transaction Controller

The Transaction Controller provides the functionality of grouping elements together, similar to Simple Controller. In
addition to that, it generates an additional entry in the listener that measures the overall time taken to perform the
nested test elements.

#### Loop Controller

The Loop Controller provides a looping mechanism. It can repeat the execution of its nested elements a specified number
of times. It also has a checkbox to configure it to loop forever.

_This controller is used to simulate high traffic to certain pages._


#### Runtime Controller

The Runtime Controller controls the duration for which its child elements are run. It executes its child elements in
its hierarchy for the specified duration. When the last of the nested elements runs, it loops through again if the
specified time for which it should run is not reached. Using the same logic, if the specified time runs out, the
Runtime Controller stops execution even if it has executed only a part of the nested elements (the currently executing
element is allowed to complete, but the newer elements are not executed once the specified time is over).

#### Throughput Controller

The Throughput Controller controls the number of executions of its child elements. This is a misnomer, as it does not
control the throughput; the Constant Throughput Timer should be used for that.

Example, in a _Thread Group_, configured to loop `10` times, if there's a child HTTP Request `A` and another child
element _Throughput Controller_, configured with a `3` as the _Total Executions_ value, and with a sub element
HTTP Request `B`; when the test is run, HTTP Request `A` will run 10 times and HTTP Request `B` will run 3 times only.
Their execution should be something like:

`A`, `B`,`A`, `B`,`A`, `B`, `A`, `A`, `A`, `A`, `A`, `A`, `A`

#### Once Only Controller

Once Only Controller executes its child elements only once per thread. This is typically used to perform logins or
another use-case that’s needed only once for a user session. There is no configuration for this Once Only Controller.
The Once Only Controller should be a child element of the thread group or Loop Controller. Otherwise, the behavior is
not defined.

#### Interleave Controller

The Interleave Controller executes only one of its child elements per loop iteration. Each time it iterates, it picks
the next child element in sequence.

A child controller is considered a sub-controller. By default, a sub-controller, including all its children, is treated
as a single unit. If the _Ignore Sub-Controller Blocks_ checkbox is enabled, the grouping implied by the sub-controller
is ignored and the child elements of the sub-controller are treated as the direct child elements of the Interleave
Controller.

The Interleave Controller is used to distribute the requests among a set of URLs.

#### Random Controller

The Random Controller is similar to the Interleave Controller except that the order of interleaving is random instead
of sequential. The configuration is just like the Interleave Controller.

#### Random Order Controller

The Random Order Controller executes all its child elements but in random order. There is no other configuration for
this controller.

#### Switch Controller

The Switch Controller is analogous to the switch/case programming construct. The Switch Controller executes only one
of its child elements after matching the element’s name with the configured Switch value. If the Switch value is an
integer, it executes the child element based on the sequence number (The sequence number starts at 0).

#### ForEach Controller

The ForEach Controller has one or more child elements over which it iterates. It can be configured with the following
parameters. For each iteration, the ForEach Controller performs the following:

- Forms a sequence number by incrementing the Start Index for loop (Exclusive) option.
- Forms the name of a user defined variable by concatenating the Input Variable Prefix, "_" and the sequence number.
- Sets the Output Variable Name to the value obtained by looking up the user defined variable. This Output Variable
Name is then available to the child elements.

The number of iterations is equal to the difference between the Start index and End index. If the Start index and the
End index have not been specified, JMeter can figure those out by looking at the user defined variables, starting with
the Input Variable Prefix string. Such variables need to be in numerical sequence. Any break in the sequence will cause
ForEach Controller to finish.

If the Add "_" Before Number? checkbox is not checked, then "_" is not used in forming the name of the user defined
variable.

Input Variable Prefix and Output Variable Name are required parameters. If you omit them, there is no error checking,
not even a log message. JMeter will simply stop execution without warning.

#### If Controller

The If Controller is useful for decision/branching logic. The configuration is simple, with only two checkboxes.

The Interpret Condition as Variable Expression? checkbox indicates whether the expression is evaluated as a JavaScript
expression (the default) or as a variable expression (compared with the string "true").

The Evaluate for All Children? checkbox indicates whether the condition should be evaluated before processing each of
the child elements. If it’s not checked, the condition is evaluated only when it is encountered for the first time.

If the condition expression has a syntax error or if the variable is not found, JMeter will simply stop execution
without any error popping up. If you have not selected the Interpret Condition as Variable Expression flag, a few DEBUG
logs are generated.

### Timers

Timers are used to simulate natural delays of users while performing some actions on web pages, introduced by the
natural flow of the uses cases (i.e.: the time required for the user to provide some input, read the information, etc.).

They are used to introduce a delay or pause before a sampler is run.

In a test plan, even if a timer is placed after the samplers, it will run before the sampler. If the timer is a child
element of a sampler, it will apply only to that sampler. Otherwise, it will apply to all the samplers in that scope.
If multiple timers are in scope, all the timers will apply before a sampler is executed.

#### Constant Timer

The Constant Timer introduces a specified delay before the samplers in its scope are executed. The only configuration
is the delay that is needed.

#### Gaussian Random Timer

The Gaussian Random Timer introduces a delay according to the Gaussian Distribution (also called the bell curve).

#### Uniform Random Timer

The delay introduced by the Uniform Random Timer has two parts:

- Constant Delay: Fixed and equal to the configured value.
- Random Delay: Varies between zero and the configured value.

The actual delay will range between the Constant Delay and the Constant Delay plus the Random Delay. As the term
"uniform" indicates, the delay varies within its range with equal probability.

#### Constant Throughput Timer

The Constant Throughput Timer calculates and introduces delays between samplers to keep the throughput at the
configured value.

#### Synchronizing Timer

The Synchronizing Timer blocks threads and releases them all at once, thus creating a large load at the same instant.
This is very helpful to test how the application handles simultaneous requests.

### Sampler

The Sampler is a component that's used to send requests to the application being tested. If the test plan has more than
one sampler, they will be executed in the order they are defined in the test plan tree.
