package xyz.joaovasques.sparkapi.messages

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object SparkApiMessages extends SprayJsonSupport with DefaultJsonProtocol {
  trait SparkApiProtocol

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

  //Unmarshalling responses
  implicit val responseFormat = jsonFormat5(SparkJobSumissionResponse)
  implicit val jobStatusResponseFormat = jsonFormat7(SparkJobStatusResponse)
  implicit val jobKillResponseFormat = jsonFormat5(SparkJobKillResponse)
}

