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
no  | column_1 | row_number
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
no  | column_1 | rank
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

It is used to get the rank of the current row without any gaps. Rows with equal values for the ranking criteria receive the same rank. The `dense_rank()` function differs from the `rank()` function in one respect; if there is a tie between two or more rows, there is no gap in the sequence of the ranked values.

For example, taking as example the first query, if now, the following query is executed

`SELECT no, column_1, dense_rank() OVER (ORDER BY no) FROM table_name;`

the result will be something similar to

```
no  | column_1 | dense_rank
----+----------+-----------
10  | data1    | 1
20  | data2    | 2
20  | data3    | 2
30  | data4    | 3
30  | data5    | 3
30  | data6    | 3
40  | data7    | 4
```

There are four different types of `no`. So, the maximum rank is 4 and the results show that:

* The row with `no` 10 has dense_rank 1
* The dense_rank value of row_number 2 and 3 with `no` 20 is 2
* The dense_rank value of row_number 4, 5, and 6 with `no` 30 is 3
* The dense_rank value of row_number 7 with `no` 40 is 4

Now, if the `column_1` is used in the `OVER` clause, there will be different ranks, as shown in the following output, because they will be all distinct values for `column_1`:

`SELECT no, column_1, dense_rank() OVER (PARTITION BY no) FROM table_name;`

```
no  | column_1 | dense_rank
----+----------+-----------
10  | data1    | 1
20  | data2    | 2
20  | data3    | 3
30  | data4    | 4
30  | data5    | 5
30  | data6    | 6
40  | data7    | 7
```

### `percent_rank()`

It is used to get the relative rank of the current row. The relative rank of the current row is calculated using the following formula:

Relative rank of the current row = (rank - 1) / (total number of rows - 1)

For example, taking as reference the same data and queries as before, if we execute

`SELECT no, column_1, percent_rank() OVER (PARTITION BY no ORDER BY column_1) FROM table_name;`

the result is

```
no  | column_1 | percent_rank
----+----------+-----------
10  | data1    | 0
20  | data2    | 0
20  | data3    | 1
30  | data4    | 0
30  | data5    | 0.5
30  | data6    | 1
40  | data7    | 0
```

The `PARTITION BY` clause was used on `no`, so `percent_rank` will be within the same `no`. This can be explained in the following manner:

* The `no` 10 has percent_rank 0
* The `no` 20 has percent_rank 0 and 1
* The `no` 30 has percent_rank 0, 0.5, and 1, and it is calculated using the preceding relative rank equation.
* The `no` 40 has percent_rank 0.

### `first_value()`

It is used to get a value evaluated at the first row of the window frame. The `first_value()` function takes the column name as the input argument.

For example, taking as reference the same data and queries as before, if we execute

`SELECT no, column_1, first_value(no) OVER (ORDER BY no, column_1) FROM table_name WHERE no > 20;`

the result is

```
no  | column_1 | first_value
----+----------+-----------
30  | data4    | 30
30  | data5    | 30
30  | data6    | 30
40  | data7    | 30
```

Now, if instead it is executed

`SELECT no, column_1, first_value(no) OVER (ORDER BY column_1 desc, no desc) FROM table_name WHERE no > 20;`

the result is

```
no  | column_1 | first_value
----+----------+-----------
40  | data7    | 40
30  | data6    | 40
30  | data5    | 40
30  | data4    | 40
```

### `last_value()`

It is used to get the value evaluated at the last row of the window frame. The last_value() function takes the column name as the input argument.

For example, taking as reference the same data and queries as before, if we execute

`SELECT no, column_1, last_value(no) OVER (ORDER BY column_1) FROM table_name;`

the result is

```
no  | column_1 | last_value
----+----------+-----------
10  | data1    | 10
20  | data2    | 20
20  | data3    | 20
30  | data4    | 30
30  | data5    | 30
30  | data6    | 30
40  | data7    | 40
```

### `nth_value()`

It is used to get a value evaluated at the row that is the _nth_ row of the window frame. The `nth_value()` function takes the column name and _nth_ number as the input argument. 

For example, taking as reference the same data and queries as before, if we execute

`SELECT no, column_1, nth_value(column_1, 2) OVER (PARTITION BY no ORDER BY column_1) FROM table_name;`

the result is

```
no  | column_1 | last_value
----+----------+-----------
10  | data1    |
20  | data2    |
20  | data3    | data3
30  | data4    |
30  | data5    | data5
30  | data6    | data5
40  | data7    |
```

In the preceding example, the `PARTITION BY` clause was used and partitioned the records based on `no`, so in every partition, the _nth_ number (in this case, number 2) will be the output of the `nth_value` function. This can be explained in the following manner:

* The `no` 10 only has one row and no second row, so the _nth_ value is `null`
* The `no` 20 has two rows and the second row has `column_1` as `data3`
* The `no` 30 has three rows and the second row has `column_1` as `data5`
* The `no` 40 has one row and no second row, so the _nth_ value is `null`

### `ntile()`
