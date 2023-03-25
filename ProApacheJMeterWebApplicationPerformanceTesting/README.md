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

A load test is a kind of performance test that’s performed at the specified load level. So ideally, we would like to
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
