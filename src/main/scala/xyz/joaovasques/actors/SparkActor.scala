package xyz.joaovasques.sparkapi.actors

import akka.actor.{ Actor, ActorLogging }
import xyz.joaovasques.sparkapi.messages.SparkApiMessages.{ JobStatus, KillJob, SubmitJob }
import akka.pattern.pipe

class SparkActor(sparkMaster: String, sparkApi: SparkApi) extends Actor with ActorLogging {
  import context.dispatcher

  def receive = active(Vector.empty)

  def active(jobs: Vector[String]): Receive = {
    case r: SubmitJob =>
      val originaSender = sender
      sparkApi submitJob(r) pipeTo originaSender

    case JobStatus(driverId) =>
      val originaSender = sender
      println("got a job status message from " + originaSender)
      sparkApi checkJobStatus(driverId) pipeTo originaSender

    case KillJob(driverId) =>
      val originaSender = sender
      sparkApi killJob(driverId) pipeTo originaSender
  }
}

