# Chapter 6: Partitioning

The process of dividing the tables into smaller manageable parts is called partitioning, and these smaller manageable parts are called partitions. In the process of partitioning, one logical big table is divided into multiple physical smaller parts.

The first and most demanding reason to use partitions in a database is to increase the performance of the database. This is achieved by partition-wise joins; if a user’s queries perform a lot of full-table scans, partitioning will help vastly, because partitions will limit the scope of this search. The second important reason to partition is ease of managing large tables.

PostgreSQL supports table partitioning through table inheritance, which means every partition will be created as a child table of a single parent table. Partitioning is performed in such a way that every child table inherits a single parent table. The parent table will be empty; it exists just to describe the whole dataset. Currently, partitioning can be implemented in **range partitioning** or **list partitioning**.

There are five simple steps used to create a partition in PostgreSQL, which are as follows:

1. Create the master table.
2. Create multiple child tables without having an overlapped table constraint.
3. Create indexes.
4. Create a trigger function to insert data into child tables.
5. Enable the constraint exclusion.

## Range Partitioning

The range partition is the partition in which we partition a table into ranges defined by a single column or multiple columns.

The table is partitioned by explicitly listing which key values appear in each partition. In list partition, each partition is defined and designated based on a column value in one set of value lists, instead of one set of adjoining ranges of values. This will be done by defining each partition by means of the values IN (value_list) syntax, where value_list is a comma-separated list of values.
