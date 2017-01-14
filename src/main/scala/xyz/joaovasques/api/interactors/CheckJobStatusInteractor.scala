package xyz.joaovasques.sparkapi.api

import xyz.joaovasques.sparkapi.messages.SparkApiMessages._
import scala.concurrent.Future
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.client.RequestBuilding
import xyz.joaovasques.sparkapi.helpers._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.StatusCodes._

class CheckJobStatusInteractor(val sparkApi: HttpRequest => Future[HttpResponse],
  val master: String, val apiVersion: String = "v1"
)(implicit system: ActorSystem) extends Interactor[String, SparkJobStatusResponse] with JsonHelpers {
  private val endpoint = s"/${apiVersion}/submissions/status"
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  private val unmarshaller: ResponseEntity => Future[SparkJobStatusResponse] =
  { entity => Unmarshal(entity).to[SparkJobStatusResponse] }

  private def handleSparkResponse(response: HttpResponse): Future[SparkJobStatusResponse] =
    response.status match {
      case OK => println(response.entity)
        unmarshaller(response.entity)
      case otherStatus =>
        throw new Exception(s"Error Communicating with Spark. Status code ${otherStatus}")
    }

  def call(driverId: String): Future[SparkJobStatusResponse] = {
    val httpRequest = RequestBuilding.Get(s"${endpoint}/${driverId}")
    sparkApi(httpRequest) flatMap { handleSparkResponse(_) }
  }
}

