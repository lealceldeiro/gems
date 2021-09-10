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
