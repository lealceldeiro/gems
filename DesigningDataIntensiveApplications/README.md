# Designing Data-Intensive Applications

Main notes taken from the book
[Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems](https://a.co/d/0Cxxz3g)

Reference links: https://github.com/ept/ddia-referencesE

Errata: https://www.oreilly.com/catalog/errata.csp?isbn=0636920032175

## Part I: Foundations of Data Systems

[Chapter 1: Reliable, Scalable, and Maintainable Applications](./Chapter1)

- Terminology and approach (i.e.: reliability, scalability, and maintainability).
- How we can try to achieve these goals.

[Chapter 2: Data Models and Query Languages](./Chapter2)

- Several different data models and query languages are compared.
- Different models are appropriate to different situations.

[Chapter 3: Storage and Retrieval](./Chapter3)

- Internals of storage engines.
- The way databases lay out data on disk.
- Choosing the right storage engine to have the best performance, based on how they are optimized for different
  workloads.

[Chapter 4: Encoding and Evolution](./Chapter4)

- Formats for data encoding (serialization)
- How these formats fare in an environment where application requirements change and schemas need to adapt over time.

## Part II: Distributed Data

[Chapter 5: Replication](./Chapter5)

[Chapter 6: Partitioning](./Chapter6)

[Chapter 7: Transactions](./Chapter7)

[Chapter 8: The Trouble with Distributed Systems](./Chapter8)

[Chapter 9: Consistency and Consensus](./Chapter9)

## Part III: Derived Data

[Chapter 10: Batch Processing](./Chapter10)

- Batch-oriented dataflow systems

[Chapter 11: Stream Processing](./Chapter11)

- Data streams

[Chapter 12: The Future of Data Systems](./Chapter12)
