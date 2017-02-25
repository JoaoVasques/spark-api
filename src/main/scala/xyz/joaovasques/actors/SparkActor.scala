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
      val originaSender = sender
      sparkApi.submitJob(r).map(_.submissionId) pipeTo originaSender

    case JobStatus(driverId) =>
      val originaSender = sender
      sparkApi.checkJobStatus(driverId).map(_.driverState) pipeTo originaSender

    case KillJob(driverId) =>
      val originaSender = sender
      sparkApi.killJob(driverId).map(_.success) pipeTo originaSender
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

  case class Ok(driverId: String)
  case class Error(driverId: String)
}

