# Stratospheric

Main notes taken from the book [Stratospheric](https://leanpub.com/stratospheric)

Source code at https://github.com/stratospheric-dev/stratospheric

## Part I. Deploying with AWS

### 3. Managing Permissions with IAM

The [IAM Console](https://console.aws.amazon.com/iam) allows us to manage IAM [users, groups](https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html), and policies.

This URL (replacing `account-ID-or-alias` in this URL with a user account ID) will automatically fill the corresponding input field in the user login form.

`https://account-ID-or-alias.signin.aws.amazon.com/console`

**Best Practices for Managing Permissions with IAM**

Amazon have their own comprehensive guide on [Security best practices in IAM](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html)

### 5 First steps with CDK

Example of deployment with a SDK app:

```shell
cdk deploy --app "./mvnw -e -q compile exec:java -Dexec.mainClass=com.example.myapp.CdkApp" -c accountId=1111112222 -c environmentName=staging -c region=eu-west-1 -c applicationName=com.example.myapp --force --require-approval never
```

Where each argument after `-c ` is a "context variable".

### 7. Building a Continuous Deployment Pipeline

**GitHub Actions Concepts***

*Steps*

A step is the smallest unit within a CI/CD pipeline built with GitHub Actions. Ideally, it executes a single command like checking out the source code, compiling the code, or running the tests. We should aim to make each step as simple as possible to keep it maintainable. We compose multiple steps into a job.

*Jobs*

A job groups multiple steps into a logical unit and puts them into a sequence. While steps within a job are executed in sequence, multiple jobs within a workflow run in parallel by default. If a job depends on the results of another job, we can mark it as dependent on that other job and GitHub Actions will run them in sequence. While all steps within a job run in the same container and filesystem, a job always starts fresh, and we will have to take care of transporting any build artifacts from one job to another, if needed.

*Workflows*

A workflow, in turn, groups multiple jobs into a logical unit. While steps and jobs are internal concepts, a workflow can be triggered by external events like a push into a repository or a webhook from some tool. A workflow can contain many jobs that run in sequence or in parallel or a mixture of both, as we see fit. GitHub Actions will show a nice visualization of the jobs within a workflow and how they depend on each other when a workflow is running.

*Workflow Runs*

A workflow run, finally, is an instance of a workflow that has run or is currently running. We can look at previous runs in the GitHub UI and see if any steps or jobs failed and look at the logs of each job.

## Part II: Spring Boot & AWS

### 8. The Sample TODO Application

| TODO App Feature                          | SpringBoot feature used | AWS service(s)                                                                                               | Additional notes                                                                                                                                                                                                                                               |
|-------------------------------------------|-------------------------|--------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Registration and Login                    | Spring Security         | AWS Cognito                                                                                                  | Users will be identified by their email addresses. The user data itself will be stored in a Cognito user pool. The app will use OIDC (OpenID Connect, an authentication framework on top of OAuth2) for retrieving and maintaining the user’s Cognito session. |
| CRUD: Viewing, Adding, and Deleting TODOs |                         | Storing the data: a a PostgreSQL databasewill be used, running on Amazon’s Relational Database Service (RDS) |                                                                                                                                                                                                                                                                |
| Sharing TODOs and Email Notifications     |                         | Amazon Simple Email Service (SES), Amazon Simple Queue Service (SQS)                                         |                                                                                                                                                                                                                                                                |
| Push Notifications                        |                         | [Apache ActiveMQ](https://activemq.apache.org/) message broker running on Amazon MQ                          | Implemented using WebSockets                                                                                                                                                                                                                                   |

**AWS-Specific Configuration**

The [AWS SDK for Java](https://aws.amazon.com/sdk-for-java/) offers several solutions for configuring access to our AWS account, such as using environment variables, a property file, or loading them from the [Amazon EC2 Instance Metadata Service](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-instance-metadata.html). Technically speaking, they are implementations of the `AWSCredentialsProvider` interface that are part of the `aws-java-sdk-core` dependency.

### 9. Local Development

A [LocalStack](https://github.com/localstack/localstack) is a fully functional local AWS stack cloud.

A local stack can be started using the following command. It and exposes the AWS S3 service on port `4566` (`0.12.2` can be replaced with a more recent version)

```shell
docker run -p 4566:4566 -e SERVICES=s3 localstack/localstack:0.12.2
```

### 10. Building User Registration and Login with Amazon Cognito

**Amazon Cognito Terminology**

A **User Pool** acts as a user directory where we can store and manage user information. Each *User Pool* comes with sign-up and sign-in functionality. This includes a web interface to sign in that we can customize and configure, for example with additional social logins (Google, GitHub, etc.) or multi-factor authentication.

A **User Pool App Client** is associated with a *User Pool* and has permission to call unauthenticated API operations like signing in or registering users. Therefore, every *App Client* requires a client ID and an optional secret.

With an **Identity Pool**, we can map a user from an *Identity Provider* to an *IAM role*. This allows us to give users access AWS resources based on their IAM permissions. Since the users of the Todo app don’t need IAM roles to use the app, an Identity Pool is not relevant in this case.

**The following output values are needed to integrate the application with Cognito properly**

- User Pool provider URL
- User Pool logout URL
- User Pool client name
- User Pool client ID
- User Pool client secret

### 11. Connecting to a Database with Amazon RDS

Amazon Relational Database Service (RDS) is the AWS service for running and managing relational databases.

Apart from PostgreSQL, MySQL, MariaDB, Oracle Database, and Microsoft SQL Server, RDS also supports Amazon’s own [Aurora](https://aws.amazon.com/rds/aurora/) database technology. Aurora is a MySQL and PostgreSQL-compatible RDBMS specifically designed with requirements of highly scalable cloud applications in mind.

RDS allows us to create and manage relational databases on AWS using its common tools and techniques such as .

In addition to using tools as the AWS CLI, IAM, and CDK to integrate the database into our AWS environment, we can manage the database through the [Amazon RDS Management Console](https://console.aws.amazon.com/rds).

### 12. Sharing Todos with Amazon SQS and Amazon SES

**Introduction to AWS SQS**

AWS SQS is a fully managed messaging service. We can use this service to pass messages between different parts of our system in a highly scalable fashion. It allows point-to-point communication where only one receiver will handle the message at a given time. AWS SQS can further help us integrate or decouple components in a distributed architecture.

Scalability, durability, and reliability of our processing with SQS depend on the queue type. AWS offers two different SQS types: FIFO and Standard.

**Dead-Letter Queues**

Each AWS SQS message has a `ReceiveCount` attribute that stores a counter and tracks how often the message has been consumed already. As part of the queue configuration, we can define a `maxReceiveCount` to specify how many processing attempts our messages have until they’re moved to a dead-letter queue.

A dead-letter queue is just another AWS SQS queue that stores unprocessable messages. The type of this queue has to match the type of source queue. In other words, a FIFO queue can only target a FIFO queue as a dead-letter queue. DLQs are optional, so we can define AWS SQS queues without specifying a DLQ.

It's a general best practice to create a DLQ for each of our processing queues due to several reasons:

- It helps to analyze and debug error scenarios.
- We isolate problematic messages for further investigations.
- We reduce the load on our system if there are multiple (if not thousands) of unprocessable messages.
- We get improved observability, as we can define alarms for our dead-letter queues to detect failures early.
- We don’t block the message processing by consuming the same faulty message over and over.

The Spring Cloud AWS Messaging module (`'org.springframework.cloud:spring-cloud-starter-aws-messaging'`) comes with the following features for both AWS SQS and AWS SNS:

- Annotation-driven listener/notification endpoints.
- Integration with the Spring Messaging API (fully for SQS, partially for SNS).
- Serialization support for messages (which SQS only knows as strings).
- Convenient access via QueueMessagingTemplate (SQS) and NotificationMessagingTemplate (SNS).

**Introduction to AWS SES**

[AWS SES (Simple Email Service)](https://docs.aws.amazon.com/ses/index.html) is an email sending and receiving service. With AWS SES, we take advantage of years of email infrastructure experience at Amazon.

**Verifying a Domain**

To send emails with AWS SES, we have to verify each identity (domain name) that we’re going to use as part of “From”, “Source”, “Sender”, or “Return-Path” to avoid unauthorized usage.

### 13. Push Notifications with Amazon MQ

The term *push notifications* usually refers to a backend server pushing information to a client rather than the client itself initiating a request - or pulling - to get information from the server.

A common design pattern for implementing such behavior is the observer pattern: multiple *observers* subscribe to a *subject* or *observable*, which in turn will notify each of its subscribers once there’s an update available.

This pattern allows us to have an `n:m` relationship between *publishers* and *subscribers* with multiple publishers publishing messages to a *topic* or *channel* and multiple subscribers receiving messages from that topic.

**AWS Services for Implementing Push Notifications**

- [Amazon Pinpoint](https://aws.amazon.com/pinpoint/): It offers email, SMS, push, or voice push notifications.

- [Amazon IoT (Core)](https://aws.amazon.com/iot-core/): While it's targeted at the use case of allowing physical objects to communicate with each other on the Internet of Things, one of the protocols used to do so is WebSocket. In theory, there’s nothing that prevents us from using Amazon IoT (hence using it WebSocket mechanims) even though we don’t have physical objects communicating with each other but rather browser clients and the people using these clients.

- [Amazon Simple Notification Service (Amazon SNS)](https://aws.amazon.com/sns/): it's a fully managed messaging service for both application-to-application (A2A) and application-to-person (A2P) communication. It’s a managed service that provides message delivery from publishers to subscribers. This only allows us to send messages to the subscriber endpoint types:
    * HTTP/HTTPS
    * email
    * Amazon Kinesis Data Firehose
    * Amazon SQS
    * a custom AWS Lambda function
    * a platform application endpoint
    * SMS
- [Amazon Simple Queue Service (SQS)](https://aws.amazon.com/sqs/): It's a fully managed message queuing service that enables you to decouple and scale microservices, distributed systems, and serverless applications.
- [Amazon MQ](https://aws.amazon.com/amazon-mq/): It's a managed message broker service for Apache ActiveMQ and RabbitMQ that makes it easy to set up and operate message brokers on AWS.

### 14. Tracing User Actions with Amazon DynamoDB

**CAP Theorem**

The CAP theorem (or “Brewer’s theorem” after computer scientist Eric Brewer) states that a distributed system can only make two of these three guarantees simultaneously:

- Consistency: Every node in a distributed system responds with the most recent data for a specific request. If an update is currently in progress the system will block the request until the update has finished.

- Availability: Every request receives a response, even if that request contains out-of-date data.

- Partition tolerance: The system continues to operate even if one or more nodes fail or messages have been dropped.

While RDBMS focus on data being consistent and available, NoSQL database systems emphasize data availability and fault tolerance.

RDBMS guarantee ACID (Atomicity, Consistency, Isolation, Durability) constraints while NoSQL database systems only guarantee data availability and fault tolerance.

NoSQL database system promise a property called eventual consistency, which can be summarised by the acronym BASE (Basically available, Soft state, Eventual consistency)

**Benefits of DynamoDB**

- It can be used as both a key-value store and a document database.
- These data storage patterns are most suitable for situations where we either have very simple data structures such as lookup tables (key-value store) or highly unstructured, even somewhat unpredictable ones (document database).
- Performance and scalability: DynamoDB can handle more than 10 trillion requests per day with up to 20 million requests per second. We can have DynamoDB automatically replicate our data across different AWS regions, too.
- Access control: DynamoDB allows for [fine-grained control](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/specifying-conditions.html) over who can access which data entries from our DynamoDB tables. We can even define subsets of attributes to be visible only to specific IAM users, groups, or roles.
- Event stream data: DynamoDB keeps a record of item-level changes made in the last 24 hours. Using Amazon Kinesis Data Streams for DynamoDB we can capture these changes and persist them, e.g. for logging or data analysis purposes.
- Encryption and automated backups: By default, DynamoDB automatically creates backups and encrypts data at rest. We can also create and restore full backups of our DynamoDB tables on demand.
- Time-to-live (TTL): This DynamoDB feature allows us to define a table column containing [individual expiration times for each item](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/howitworks-ttl.html). When the value defined in this column (as a UNIX Epoch timestamp) is less than the current timestamp the entry will be deleted automatically with no further business logic required.

**Two main principles for designing NoSQL schemas**

- Don’t design your schema until you know the specific queries you want it to respond to.
- Use as few tables as possible and try to keep related data together (described as [single-table-design](https://www.alexdebrie.com/posts/dynamodb-single-table/)).

## Part III: Production Readiness with AWS

### 15. Structured Logging with Amazon CloudWatch

A *log stream* is a stream of log events that is emitted by a certain component.

Log streams are aggregated into *log groups*.

*CloudWatch Insights* is a service that provides a UI and a powerful query language to search through one or more log groups.

### 16. Metrics with Amazon CloudWatch

**Amazon ECS**


Key metrics to monitor:

- `CPUUtilization`: The percentage of CPU units used by the cluster or a service in the cluster.
- `MemoryUtilization`: The percentage of main memory (RAM) used by the cluster or a service in the cluster.

**AWS ELB**

AWS Elastic Load Balancing is the main entry point from the internet to our application. When debugging a scenario where requests don’t arrive at our backend, it’s important to look at the ELB metrics.

Key metrics to monitor:

- `HTTPCode_ELB_2XX_Count` (and *3XX*, *4XX*, *5XX*): The number of HTTP response codes returned by the load balancer.
- `TargetResponseTime`: The time elapsed, in seconds, after the request leaves the load balancer until a response from the target is received.
- `RequestCount`: The number of requests processed over IPv4 and IPv6.

**Amazon Cognito**

With Amazon Cognito as the identity provider, we can track and monitor the sign-ins and sign-ups per UserPool client.

Key metrics to monitor:

- `SignUpSuccess`: The number of successful user registration requests for a particular user pool.
- `SignInSuccess`: The number of successful user authentication requests made to a particular user pool.

**Amazon SQS*

Amazon SQS provides several metrics for inspecting the approximate queue size and the age of the oldest message. This helps with understanding if there’s an issue with our processing.Furthermore, it’s important to keep track of the number of messages inside our dead-letter queue to identify failed message consumption.

Key metrics to monitor:

- `NumberOfMessagesReceived`: The number of messages returned by calls to the `ReceiveMessage` action.
- `NumberOfMessagesDeleted`: The number of messages deleted from a queue.
- `ApproximateAgeOfOldestMessage`: The approximate age of the oldest non-deleted message in a queue.
- `ApproximateNumberOfMessagesVisible`: The number of messages available for retrieval from a queue.

**Amazon RDS**

Monitoring the database helps to either disqualify or identify the database as the possible performance bottleneck.

Key metrics to monitor:

- `CPUUtilization`: The percentage of CPU utilization.
- `ReadIOPS`: The average number of disk read I/O operations per second.
- `WriteIOPS`: The average number of disk write I/O operations per second.
- `DatabaseConnections`: The number of database connections currently in use.
- `FreeStorageSpace`: The amount of available storage space on disk.

**Amazon DynamoDB**

With Amazon DynamoDB, we have to keep track of how we utilize the configured read and write capacity. We need to spring into action if we see constant throttle events because we exceed our configured capacity.

Key metrics to monitor:

- `ReadThrottleEvents`: The number of requests that exceed the provisioned read capacity units for a table or a global secondary index.
- `WriteThrottleEvents`: The number of requests that exceed the provisioned write capacity units for a table or a global secondary index.

**Amazon SES**

Deliverability and bounce rates are key metrics to monitor when sending emails. Just because our application successfully sent an email to a user doesn’t mean the message arrived in their inbox.

Key metrics to monitor:

- `Delivery`: The number of successfully delivered emails to the recipient’s email server.
- `Reputation.BounceRate`: The bounce rate for our account. This includes both hard (the email address doesn’t exist), and soft (the recipient’s address is temporarily unable to receive messages) bounces.
- `Send`: The number of send email attempts from our account.

**Amazon MQ**

Key metrics to monitor:

- `CpuUtilization`: The percentage of allocated Amazon EC2 compute units that the broker currently uses.
- `MessageCount`: The total number of ready and unacknowledged messages in the queues.
- `TotalMessageCount`: The number of messages stored on the broker.
- `TotalConsumerCount`: The number of message consumers subscribed to destinations on the current broker.
- `CurrentConnectionsCount`: The current number of active connections on the current broker.

**Amazon S3**

Key metrics to monitor:

- `BucketSizeBytes`: The amount of data in bytes stored in a bucket.
- `NumberOfObjects`: The number of objects stored in a bucket for all storage classes

**AWS Lambda**

As this is a crucial part of our CI/CD pipeline, we should keep a close look at the outcome of the Lambda invocations. A Lambda might fail due to an error or a timeout, and as a result, we won’t be able to deploy to production.

Key metrics to monitor:

- `Invocations`: The number of total invocations for a function.
- `Duration`: The amount of time in milliseconds for an invocation.
- `Errors`: The number of invocations that result in an error.

### 17. Alerting with Amazon CloudWatch

With Amazon CloudWatch, we can create two types of alarms:

- *Metric alarms*: We continuously evaluate a single CloudWatch metric or the result of a mathematical expression based on up to ten CloudWatch metrics.
- *Composite alarms*: We consider the state of several alarms and define a logical expression on them (for example ALARM("alarm-a") AND (ALARM("alarm-b") OR ALARM("alarm-c))).

Once an alarm transitions into a new state, we can trigger automatic actions with Amazon CloudWatch.
