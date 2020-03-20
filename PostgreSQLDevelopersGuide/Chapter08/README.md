# Chapter 8. Dealing with Large Objects

Objects that require huge storage size and can’t be supported by simple available data types such as `int` and `varchar` are usually referred to as Large Objects (LOs) or Binary Large OBjects (BLOBs).

## Why large objects?

Based on the structure or type of data, object types can be categorized as:

* Simple: related to data that can be organized in simple tables and data types
* Complex: deals with requirements such as that of user-defined data types
* Semi-structured: forms of data types such as XML and JSON fit into this category
* Unstructured: other formats such as images, audios, and videos cannot be stored in the previous mentioned data types because they cannot be broken into smaller logical structures for interpretation by standard means. So they need to be stored as unstructured data and require a different mechanism to handle them.

## PostgreSQL large objects

PostgreSQL provides two ways to store large objects:

* Implementation of the BYTEA data type
* Implementation of large object storage
