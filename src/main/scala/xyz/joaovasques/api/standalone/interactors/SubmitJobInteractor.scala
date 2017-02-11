package xyz.joaovasques.sparkapi.api.standalone.interactors

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

class SubmitJobInteractor(val sparkApi: HttpRequest => Future[HttpResponse],
  val master: String, val apiVersion: String = "v1"
)(implicit system: ActorSystem) extends Interactor[SubmitJob, SparkJobSumissionResponse] with JsonHelpers {

  private val endpoint = s"/${apiVersion}/submissions/create"
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  private val unmarshaller: ResponseEntity => Future[SparkJobSumissionResponse] =
  { entity => Unmarshal(entity).to[SparkJobSumissionResponse] }

  private def handleSparkResponse(response: HttpResponse): Future[SparkJobSumissionResponse] =
    response.status match {
      case OK =>
        for(unmarshalledResponse <- unmarshaller(response.entity)) yield {
          if(unmarshalledResponse.success)
            unmarshalledResponse
          else
          throw new Exception("Job Failed")
        }
      case otherStatus =>
        throw new Exception(s"Communicating with Spark: status code ${otherStatus}")
    }

  def call(request: SubmitJob): Future[SparkJobSumissionResponse] = {
    val httpRequest = RequestBuilding.Post(endpoint,
      HttpEntity(ContentTypes.`application/json`, generateSubmitJsonBody(request, master, request.envVars)))
      .addHeader(header)

    sparkApi(httpRequest) flatMap { handleSparkResponse(_) }
  }
}

