# CHAPTER 9: Building Data Pipelines

The main value Kafka provides to data pipelines is its ability to serve as a very large, reliable buffer between various
stages in the pipeline.

## Run Kafka Connect

`bin/connect-distributed.sh config/connect-distributed.properties`

The [Debezium Project](https://debezium.io/) provides a collection of high-quality, open source, change capture
connectors for a variety of databases.

Kafka can be looked at as a platform that can handle data integration (with Connect), application integration (with
producers and consumers), and stream processing. Kafka could be a viable replacement for an ETL tool that only
integrates data stores.
