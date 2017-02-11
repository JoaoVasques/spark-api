package xyz.joaovasques.sparkapi.messages

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object SparkApiMessages extends SprayJsonSupport with DefaultJsonProtocol {
  type EnvVars = Map[String, String]
  trait SparkApiProtocol

  //Requests
  trait SparkRequest extends SparkApiProtocol

  case class SubmitJob(name: String,
    mainClass: String,
    arguments: Set[String],
    jarLocation: String,
    envVars: Map[String, String],
    sparkProperties: Map[String, String] = Map()
  ) extends SparkRequest

  case class JobStatus(driverId: String) extends SparkRequest
  case class KillJob(driverId: String) extends SparkRequest

  //Responses
  trait SparkResponse

  case class SparkJobSumissionResponse(action: String,
    message: String,
    submissionId: String,
    serverSparkVersion: String,
    success: Boolean
  ) extends SparkApiProtocol with SparkResponse

  case class SparkJobStatusResponse(action: String,
    driverState: String,
    serverSparkVersion: String,
    submissionId: String,
    success: Boolean,
    workerHostPort: Option[String],
    workerId: Option[String]
  ) extends SparkApiProtocol with SparkResponse

  case class SparkJobKillResponse(action: String,
    message: String,
    serverSparkVersion: String,
    submissionId: String,
    success: Boolean
  ) extends SparkApiProtocol with SparkResponse

  case class Ok(driverId: String) extends SparkResponse
  case class Error(driverId: String) extends SparkResponse

  //Unmarshalling responses
  implicit val responseFormat = jsonFormat5(SparkJobSumissionResponse)
  implicit val jobStatusResponseFormat = jsonFormat7(SparkJobStatusResponse)
  implicit val jobKillResponseFormat = jsonFormat5(SparkJobKillResponse)
}

