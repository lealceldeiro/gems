# Chapter 7: Structuring Data with Java

## 7.1 Using Arrays for Data Structuring

### Problem

You need to keep track of a fixed amount of information and retrieve it (usually) sequentially.

### Solution

Use an array.

## 7.4 Like an Array, but More Dynamic

### Problem

You don’t want to worry about storage reallocation (often because you don’t know how big the incoming dataset is going to be); you want a standard class to handle it for you.

### Solution

Use a `List` implementation or one of the other *Collections* classes, along with Java’s Generic Types mechanism, declaring the Collection with a type parameter identifying the type of your data.
