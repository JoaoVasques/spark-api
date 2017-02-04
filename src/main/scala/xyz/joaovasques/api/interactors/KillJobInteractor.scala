package xyz.joaovasques.sparkapi.api

import scala.concurrent.Future

import akka.actor.ActorSystem
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, HttpResponse, ResponseEntity}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import xyz.joaovasques.sparkapi.helpers._
import xyz.joaovasques.sparkapi.messages.SparkApiMessages._

class KillJobInteractor(val sparkApi: HttpRequest => Future[HttpResponse],
  val master: String, val apiVersion: String = "v1"
)(implicit system: ActorSystem) extends Interactor[String, SparkJobKillResponse] with JsonHelpers {

  private val endpoint = s"/${apiVersion}/submissions/kill"
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  private val unmarshaller: ResponseEntity => Future[SparkJobKillResponse] =
  { entity => Unmarshal(entity).to[SparkJobKillResponse] }

  private def handleSparkResponse(response: HttpResponse): Future[SparkJobKillResponse] =
    response.status match {
      case OK => unmarshaller(response.entity)
      case otherStatus =>
        Future.failed(new Exception(s"Communicating with Spark: status code ${otherStatus}"))
    }

  def call(driverId: String): Future[SparkJobKillResponse] = {
    val httpRequest = RequestBuilding.Post(s"${endpoint}/${driverId}", HttpEntity(ContentTypes.`application/json`, ""))
      .addHeader(header)

    sparkApi(httpRequest) flatMap { handleSparkResponse(_) }
  }
}

