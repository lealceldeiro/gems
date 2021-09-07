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
