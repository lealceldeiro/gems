# Chapter 11: Performance and Scalability

## 11.1 Thinking about performance

Improving performance means doing more work with fewer resources.

When the performance of an activity is limited by availability of a particular resource, we say it is bound by that resource: CPU-bound, database-bound, etc.

In using concurrency to achieve better performance, we are trying to do two things: utilize the processing resources we have more effectively, and enable our program to exploit additional processing resources if they become available.

### 11.1.1 Performance versus scalability

Scalability describes the ability to improve throughput or capacity when additional computing resources (such as additional CPUs, memory, storage, or I/O bandwidth) are added.

### 11.1.2 Evaluating performance tradeoffs

Most optimizations are premature: _they are often undertaken before a clear set of requirements is available_.

Avoid premature optimization. First make it right, then make it fast—if it is not already fast enough.

Before deciding that one approach is “faster” than another, ask yourself some questions:

* What do you mean by “faster”?
* Under what conditions will this approach actually be faster? Under light or heavy load? With large or small data sets? Can you support your answer with measurements?
* How often are these conditions likely to arise in your situation? Can you support your answer with measurements?
* Is this code likely to be used in other situations where the conditions may be different?
* What hidden costs, such as increased development or maintenance risk, are you trading for this improved performance? Is this a good tradeoff?

Measure, don’t guess.

* The free _perfbar_ application can provide a good picture of how busy the CPUs are.

## 11.2 Amdahl’s law
