package xyz.joaovasques.sparkapi.api.standalone

import scala.concurrent.Future

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import xyz.joaovasques.sparkapi.messages.SparkApiMessages._
import xyz.joaovasques.sparkapi.helpers._
import xyz.joaovasques.sparkapi.api._
import xyz.joaovasques.sparkapi.api.standalone.interactors._
import xyz.joaovasques.sparkapi.actors.SparkActor._

private[sparkapi] case class SparkApiStandlone(
  master: String, apiVersion: String = "v1"
)(implicit system: ActorSystem) extends SparkApi with JsonHelpers {

  override val port = 6066
  private implicit val materializer = ActorMaterializer()

  private lazy val connectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http().outgoingConnection(master, port)

  private def apiRequest(request: HttpRequest): Future[HttpResponse] =
    Source.single(request).via(connectionFlow).runWith(Sink.head)


  private val submitJobInteractor = new SubmitJobInteractor(apiRequest, master, apiVersion)
  private val jobStatusInteractor = new CheckJobStatusInteractor(apiRequest, master, apiVersion)
  private val killJobInteractor = new KillJobInteractor(apiRequest, master, apiVersion)


  def submitJob(req: SubmitJob): Future[SparkJobSumissionResponse] = submitJobInteractor.call(req)

  def checkJobStatus(driverId: String): Future[SparkJobStatusResponse] = jobStatusInteractor.call(driverId)

  def killJob(driverId: String): Future[SparkJobKillResponse] = killJobInteractor.call(driverId)
}

