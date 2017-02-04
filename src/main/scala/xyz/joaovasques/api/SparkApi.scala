package xyz.joaovasques.sparkapi.actors

import scala.concurrent.Future

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import xyz.joaovasques.sparkapi.messages.SparkApiMessages._
import xyz.joaovasques.sparkapi.helpers._
import xyz.joaovasques.sparkapi.api._

private[actors] trait SparkApi {

  type ApiResponse = (SparkResponse, ActorRef)

  val port: Int

  def submitJob(request: SubmitJob): Future[SparkJobSumissionResponse]
  def checkJobStatus(driverId: String): Future[SparkJobStatusResponse]
  def killJob(driverId: String): Future[SparkResponse]
}
