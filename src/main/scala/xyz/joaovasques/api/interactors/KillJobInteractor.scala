package xyz.joaovasques.sparkapi.api

import xyz.joaovasques.sparkapi.messages.SparkApiMessages._
import scala.concurrent.Future
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model.{ HttpHeader, ResponseEntity }
import akka.http.scaladsl.model.HttpHeader.ParsingResult
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import xyz.joaovasques.sparkapi.helpers._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.StatusCodes._

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

