# Chapter 1: Reliable, Scalable, and Maintainable Applications

Reliability

- Tolerating hardware and software fault
- Human error

Scalability

- Measuring load and performance
- Latency percentiles, throughput

Maintainability

- Operability, simplicity and evolvability

A data-intensive application is typically built from standard building blocks that provide commonly needed
functionality. For example, many applications need to:

- Store data so that they, or another application, can find it again later (databases)
- Remember the result of an expensive operation, to speed up reads (caches)
- Allow users to search data by keyword or filter it in various ways (search indexes)
- Send a message to another process, to be handled asynchronously (stream processing)
- Periodically crunch a large amount of accumulated data (batch processing)

Reliability means making systems work correctly, even when faults occur. Faults can be in hardware (typically random
and uncorrelated), software (bugs are typically systematic and hard to deal with), and humans (who inevitably make
mistakes from time to time). Fault-tolerance techniques can hide certain types of faults from the end user.

Typical expectations for a _reliable_ software include:

- The application performs the function that the user expected.
- It can tolerate the user making mistakes or using the software in unexpected ways.
- Its performance is good enough for the required use case, under the expected load and data volume.
- The system prevents any unauthorized access and abuse.

Scalability means having strategies for keeping performance good, even when load increases. In order to discuss
scalability, we first need ways of describing load and performance quantitatively.

_Scalability_ is the term we use to describe a system's ability to cope with increased load.

_Load_ can be described with a few numbers which we call _load parameters_ (i.e.: requests per second to a web server,
the ratio of reads to writes in a database, the number of simultaneously active users in a chat room, the hit rate on
a cache, etc.).

_Percentiles_ are a good way to measure service response time.

An architecture that is appropriate for one level of load is unlikely to cope with 10 times that load.

The architecture of systems that operate at large scale is usually highly specific to the application — there is no
such thing as a generic, one-size-fits-all scalable architecture (informally known as magic scaling sauce).

An architecture that scales well for a particular application is built around assumptions of which operations will be
common and which will be rare—the load parameters.


Maintainability has many facets, but in essence it's about making life better for the engineering and operations teams
who need to work with the system. Good abstractions can help reduce complexity and make the system easier to modify and
adapt for new use cases. Good operability means having good visibility into the system’s health, and having effective
ways of managing it.

Three design principles for software systems to design them in such a way that it will hopefully minimize pain during
maintenance:

- Operability: Make it easy for operations teams to keep the system running smoothly.
- Simplicity: Make it easy for new engineers to understand the system, by removing as much complexity as possible from
  the system.
- Evolvability: Make it easy for engineers to make changes to the system in the future, adapting it for unanticipated
  use cases as requirements change. Also known as extensibility, modifiability, or plasticity.
