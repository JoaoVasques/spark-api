# Spark API

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/10ab7014e35e476cb29be6c39e5069c4)](https://www.codacy.com/app/joaovasques_716/spark-api?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=JoaoVasques/spark-api&amp;utm_campaign=Badge_Grade) [![Build Status](https://travis-ci.org/JoaoVasques/spark-api.svg?branch=master)](https://travis-ci.org/JoaoVasques/spark-api)

## Introduction

I use [Apache Spark](http://spark.apache.org/) a lot in my daily work but also on some personal experiments. 

I build this library for people who use Akka (like myself) and would like to have an easy way to interact with Spark to submit, monitor and kill Spark Jobs without having to deploy a web server and interact with a REST API.

Hope this serves you well. Contributions are more than welcome.

## Instalation

To use just add to your `build.sbt`

```
resolvers += Resolver.jcenterRepo

libraryDependencies+= "xyz.joaovasques" % "spark-api_2.11" % "0.2"
```

## Features

The main purpose of this library is to simplify the management of Spark Jobs. Currently, only Standalone Cluster mode is supported. The current version of this library supports the following operations

+ Submit a job;
+ Check a job status;
+ Kill a running job.

This library is built on top of Akka and uses [Akka-Http](http://doc.akka.io/docs/akka-http/current/scala.html) to communicate with Spark's master.

## Usage

### Spark Actor

The `SparkApi` object is the main point of the this library. In order to start sending commands to the cluster you need to create a Spark Actor.

```scala
def getStandaloneGateway(sparkMaster: String)(implicit system: ActorSystem): ActorRef
```

### Submit a Job

In order to submit a job to Spark you need to send a `SubmitJob` message to the spark actor. 

```scala

case class SubmitJob(name: String, mainClass: String, arguments: Set[String], jarLocation: String, envVars: EnvVars) extends SparkRequest
```

The following code snipet demonstrates how to submit a job to spark

```scala
import xyz.joaovasques.sparkapi.messages._
import xyz.joaovasques.sparkapi._

val sparkActor = ....

val request = SubmitJob("TestJob", "com.example.test", Set("--env", "test"), "s3n://...", Map())

(sparkActor ? request).mapTo[SparkApiResponse]
```

The actor returns a ```Ok(driverId: String)``` if successful and an exception otherwise.

### Check Job Status

In order to check the status of a job you need to send a `JobStatus` message to the spark actor. 

```scala
case class JobStatus(driverId: String) extends SparkRequest
```

The following code snipet demonstrates how to check a job status

```scala
import xyz.joaovasques.sparkapi.messages._
import xyz.joaovasques.sparkapi._

val sparkActor = ....

val request = JobStatus("driverid-20170302629")

(sparkActor ? request).mapTo[String]
```


### Kill a Job

In order to check the status of a job you need to send a `JobStatus` message to the spark actor. 

```scala
case class KillJob(driverId: String) extends SparkRequest
```

The following code snipet demonstrates how to kill a Spark job.

```scala
import xyz.joaovasques.sparkapi.messages._
import xyz.joaovasques.sparkapi._

val sparkActor = ....

val request = KillJob("driverid-20170302629")

(sparkActor ? request).mapTo[SparkApiResponse]
```

## Future work

Some future work might include:

1. Support for a job watcher that tracks the health of a running job;
2. Improve Spark Actor Message Protocol (some additional types can be added)
2. Expose Spark internal job metrics
3. Add support for other cluster management systems (e.g. Mesos)
4. Integrate with Akka Clustering?! 
5. Whatever **YOU** like :)
