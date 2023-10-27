# Chapter 3: Storage and Retrieval

Storage engines fall into two broad categories: those optimized for transaction processing (OLTP), and those optimized
for analytics (OLAP).

OLTP systems are typically user-facing, which means that they may see a huge volume of requests. In order to handle the
load, applications usually only touch a small number of records in each query. The application requests records using
some kind of key, and the storage engine uses an index to find the data for the requested key. Disk seek time is often
the bottleneck here.

Data warehouses and similar analytic systems are less well known, because they are primarily used by business analysts,
not by end users. They handle a much lower volume of queries than OLTP systems, but each query is typically very
demanding, requiring many millions of records to be scanned in a short time. Disk bandwidth (not seek time) is often the
bottleneck here, and column-oriented storage is an increasingly popular solution for this kind of workload.

On the OLTP side, there are storage engines from two main schools of thought:

1. The log-structured school, which only permits appending to files and deleting obsolete files, but never updates a
   file that has been written.
2. The update-in-place school, which treats the disk as a set of fixed-size pages that can be overwritten.
