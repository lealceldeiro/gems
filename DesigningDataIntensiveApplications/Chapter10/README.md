# PART III - Derived Data

On a high level, systems that store and process data can be grouped into two broad categories:

- Systems of record
- Derived data systems

## Chapter 10: Batch Processing

In the Unix world, the uniform interface that allows one program to be composed with another is files and pipes; in
MapReduce, that interface is a distributed filesystem.

Dataflow engines add their own pipe-like data transport mechanisms to avoid materializing intermediate state to the
distributed filesystem, but the initial input and final output of a job is still usually HDFS.

The two main problems that distributed batch processing frameworks need to solve are:

- Partitioning: In MapReduce, mappers are partitioned according to input file blocks. The output of mappers is
  repartitioned, sorted, and merged into a configurable number of reducer partitions.
- Fault tolerance: MapReduce frequently writes to disk, which makes it easy to recover from an individual failed task
  without restarting the entire job but slows down execution in the failure-free case. Dataflow engines perform less
  materialization of intermediate state and keep more in memory, which means that they need to recompute more data if
  a node fails. Deterministic operators reduce the amount of data that needs to be recomputed.

Join algorithms for MapReduce, most of which are also internally used in MPP databases and dataflow engines:

- Sort-merge joins: Each of the inputs being joined goes through a mapper that extracts the join key. By partitioning,
  sorting, and merging, all the records with the same key end up going to the same call of the reducer. This function
  can then output the joined records.
- Broadcast hash joins: One of the two join inputs is small, so it is not partitioned and it can be entirely loaded
  into a hash table. Thus, you can start a mapper for each partition of the large join input, load the hash table for
  the small input into each mapper, and then scan over the large input one record at a time, querying the hash table for
  each record.
- Partitioned hash joins: If the two join inputs are partitioned in the same way (using the same key, same hash
  function, and same number of partitions), then the hash table approach can be used independently for each partition.

Distributed batch processing engines have a deliberately restricted programming model: callback functions (such as
mappers and reducers) are assumed to be stateless and to have no externally visible side effects besides their
designated output.

