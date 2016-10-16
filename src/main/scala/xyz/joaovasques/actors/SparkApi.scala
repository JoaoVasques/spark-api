package xyz.joaovasques.sparkapi.actors

import akka.http.scaladsl.model.ResponseEntity
import scala.concurrent.Future

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import xyz.joaovasques.sparkapi.messages.SparkApiMessages._

private[actors] trait SparkApi {

  type ApiResponse = (SparkResponse, ActorRef)

  val port: Int

  def submitJob(request: SubmitJob): Future[String]
  def checkJobStatus(driverId: String): Future[SparkResponse]
  def killJob(driverId: String): Future[SparkResponse]
}

case class SparkApiStandlone(
  master: String, apiVersion: String = "v1"
)(implicit system: ActorSystem) extends SparkApi {

  // Spark Endpoints
  private val SUBMIT_ENDPOINT= s"${apiVersion}/submissions/create"
  private val STATUS_ENDPOINT= s"${apiVersion}/submissions/status"
  private val KILL_ENDPOINT = s"${apiVersion}/submissions/kill"

  override val port = 6066
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  private lazy val connectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http().outgoingConnection(master, port)

  private def apiRequest(request: HttpRequest): Future[HttpResponse] =
    Source.single(request).via(connectionFlow).runWith(Sink.head)

  private val defaultUmarshaller: ResponseEntity => Future[SparkResponseContent] = { entity => Unmarshal(entity).to[SparkResponseContent] }

  private def handleSparkResponse(response: HttpResponse, unmarshaller: ResponseEntity => Future[SparkResponseContent]): Future[SparkResponse] = {
    response.status match {
      case OK => unmarshaller(response.entity)
          .map {r => if(r.success) Ok(r.driverId) else Error(r.driverId) }
      case otherStatus =>
        Future.failed(new Exception(s"Communicating with Spark: status code ${otherStatus}"))
    }
  }

  def submitJob(request: SubmitJob): Future[String] = {
//     curl -X POST http://spark-cluster-ip:6066/v1/submissions/create --header "Content-Type:application/json;charset=UTF-8" --data '{
//   "action" : "CreateSubmissionRequest",
//   "appArgs" : [ "myAppArgument1" ],
//   "appResource" : "file:/myfilepath/spark-job-1.0.jar",
//   "clientSparkVersion" : "1.5.0",
//   "environmentVariables" : {
//     "SPARK_ENV_LOADED" : "1"
//   },
//   "mainClass" : "com.mycompany.MyJob",
//   "sparkProperties" : {
//     "spark.jars" : "file:/myfilepath/spark-job-1.0.jar",
//     "spark.driver.supervise" : "false",
//     "spark.app.name" : "MyJob",
//     "spark.eventLog.enabled": "true",
//     "spark.submit.deployMode" : "cluster",
//     "spark.master" : "spark://spark-cluster-ip:6066"
//   }
// }'
    ???
  }

  def checkJobStatus(driverId: String): Future[SparkResponse] = {
    val statusUrl = s"$STATUS_ENDPOINT/$driverId"
    apiRequest(RequestBuilding.Get(statusUrl)) flatMap { handleSparkResponse(_, defaultUmarshaller) }
  }

  def killJob(driverId: String): Future[SparkResponse] = {
    val killUrl = s"$KILL_ENDPOINT/$driverId"
    apiRequest(RequestBuilding.Post(killUrl)) flatMap { handleSparkResponse(_, defaultUmarshaller) }
  }
}

