# Spark API

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/10ab7014e35e476cb29be6c39e5069c4)](https://www.codacy.com/app/joaovasques_716/spark-api?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=JoaoVasques/spark-api&amp;utm_campaign=Badge_Grade)

## Introduction


I use Spark a lot in my daily work but also on some personal experiments. I never liked how non-clear is the 
process of submitting jobs to a Spark Cluster (in Standalone Mode). 

I decided to dig a little bit deeper into Spark and 
discovered they have an internal REST API they are not exposing or documenting. This REST API is used on the 
`spark-submit` shell script.

## Features

The main purpose of this library is to simplify the management of Spark Jobs. Currently, only Standalone Cluster mode is supported. The current version of this library supports the following operations

+ submit a job;
+ check a job status;
+ kill a running job.

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
type EnvVars = Map[String, String]

case class SubmitJob(name: String, mainClass: String, arguments: Set[String], jarLocation: String, envVars: EnvVars)
```

The following code snipet demonstrates how to submit a job to spark

```scala
import xyz.joaovasques.sparkapi.messages._
import xyz.joaovasques.sparkapi.._

val sparkActor = ....

val request = SubmitJob("TestJob", "com.example.test", Set("--env", "test"), "s3n://...", Map())

(sparkActor ? request).mapTo[SparkJobSumissionResponse]
```

The actor returns a response class named `SparkJobSumissionResponse` that has the following signature.

```scala
 case class SparkJobSumissionResponse(action: String,
    message: String,
    submissionId: String,
    serverSparkVersion: String,
    success: Boolean
  ) extends SparkApiProtocol with SparkResponse
```

### Check Job Status

### Kill a Job


## Future work

Some future work might include:

1. Support for a job watcher that tracks the health of a running job;
2. Expose Spark internal job metrics
3. Add support for other cluster management systems (e.g. Mesos)
4. Integrate with Akka Clustering?! 
5. Whatever **YOU** like :)
