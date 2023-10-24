# Designing Data-Intensive Applications

Main notes taken from the book
[Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems](https://a.co/d/0Cxxz3g)

Reference links: https://github.com/ept/ddia-referencesE

Errata: https://www.oreilly.com/catalog/errata.csp?isbn=0636920032175

Chapter 1

- Terminology and approach (i.e.: reliability, scalability, and maintainability).
- How we can try to achieve these goals.

Chapter 2

- Several different data models and query languages are compared.
- Different models are appropriate to different situations.

Chapter 3

- Internals of storage engines.
- The way databases lay out data on disk.
- Choosing the right storage engine to have the best performance, based on how they are optimized for different
  workloads.

Chapter 4

- Formats for data encoding (serialization)
- How these formats fare in an environment where application requirements change and schemas need to adapt over time.

Part II

- Issues of distributed data systems
