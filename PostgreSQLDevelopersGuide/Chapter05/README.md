# Chapter 5: Window Functions

[Window functions](https://www.postgresql.org/docs/current/functions-window.html) are built-in functions provided by PostgreSQL. They are used for calculation between multiple rows, which are related to the current query row.

They are used to perform advance sorting and limit the number of rows returned on a subset of a joined table of data.

### `cume_dist()`

It is used to get the relative rank of the current row. It is calculated by dividing the number of rows preceding the current row by the total number of rows. i.e., supposing we execute the following query

`SELECT no, column_1 FROM table_name;`

and it gives
```
no  | column_1
----+---------
10  | data1
20  | data2
20  | data3
30  | data4
30  | data5
30  | data6
40  | data7
```
If the following query is executed `SELECT no, column_1, cume_dist() OVER (ORDER BY no) FROM table_name;` the result will be something similar to
```
no  | column_1 | cume_dist
----+----------+-----------
10  | data1    | 0.14
20  | data2    | 0.42
20  | data3    | 0.42
30  | data4    | 0.85
30  | data5    | 0.85
30  | data6    | 0.85
40  | data7    | 1
```
As the OVER clause was used on `no`, the `cume_dist()` function will assign the same rank to rows that have the same d`no` value. The results show that:

* The rank of the first row with department number 10 is 1/7 = 0.14
* The rank of the second and third rows with department number 20 is 3/7 = 0.42
* The rank of the fourth, fifth, and sixth rows is 6/7 = 0.85
* The rank of the seventh row is 7/7 = 1

### `row_number()`

It is used to get the number of the current row within its partition, starting from 1.

For example, taking as example the first query, if now, the following query is executed

`SELECT no, column_1, row_number() OVER (PARTITION BY no) FROM table_name;`

the result will be something similar to
```
no  | column_1 | cume_dist
----+----------+-----------
10  | data1    | 1
20  | data2    | 1
20  | data3    | 2
30  | data4    | 1
30  | data5    | 2
30  | data6    | 3
40  | data7    | 1
```

The result of the table shows the number of rows based on the `no` partition, which can be explained in the following manner:

* The `no` 10 has only one row with row_number 1
* The `no` 20 has two rows starting from number 1 up to number 2
* The `no` 30 has three rows starting from number 1 up to number 3
* The `no` 40 has only one row with row_number 1

### ` rank()`

It is used to get the ranks of the current row with a gap.

For example, taking as example the first query, if now, the following query is executed

`SELECT no, column_1, rank() OVER (PARTITION BY no ORDER BY column_1) FROM table_name;`

the result will be something similar to
```
no  | column_1 | cume_dist
----+----------+-----------
10  | data1    | 1
20  | data2    | 1
20  | data3    | 2
30  | data4    | 1
30  | data5    | 2
30  | data6    | 3
40  | data7    | 1
```

The result of the table shows the number of rows based on the `no` partition, which can be explained in the following manner:

* The `no` 10 got rank 1 based on `column_1`, that is, `data1`.
* The `no` 20 got rank 1 and 2 based on `column_1`, that is, `data2` and `data3`
* The `no` 30 got rank 1, 2, and 3 based on three different `column_1`, that is, `data4`, `data5`, and `data6`
* The `no` 40 got rank 1 based on `column_1`, that is, `data7`

### `dense_rank()`
