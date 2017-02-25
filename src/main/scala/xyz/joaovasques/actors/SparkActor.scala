package xyz.joaovasques.sparkapi.actors

import akka.actor.{ Actor, ActorLogging }
import xyz.joaovasques.sparkapi.api.SparkApi
import akka.pattern.pipe

class SparkActor(sparkMaster: String, sparkApi: SparkApi) extends Actor with ActorLogging {
  import context.dispatcher
  import SparkActor._

  def receive = active(Vector.empty)

  def active(jobs: Vector[String]): Receive = {
    case r: SubmitJob =>
      sparkApi.submitJob(r).map(result => Ok(result.submissionId)) pipeTo sender

    case JobStatus(driverId) =>
      sparkApi.checkJobStatus(driverId).map(_.driverState) pipeTo sender

    case KillJob(driverId) =>
      val futureResponse = sparkApi.killJob(driverId).map {response =>
        if(response.success) Ok(response.submissionId) else Error(response.message)
      }

      futureResponse pipeTo sender
  }
}

object SparkActor {
  trait SparkRequest

  case class SubmitJob(name: String,
    mainClass: String,
    arguments: Set[String],
    jarLocation: String,
    envVars: Map[String, String],
    sparkProperties: Map[String, String] = Map()
  ) extends SparkRequest

  case class JobStatus(driverId: String) extends SparkRequest
  case class KillJob(driverId: String) extends SparkRequest


  trait SparkApiResponse

  case class Ok(driverId: String) extends SparkApiResponse
  case class Error(driverId: String) extends SparkApiResponse
}

