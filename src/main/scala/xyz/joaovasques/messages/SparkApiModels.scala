package xyz.joaovasques.sparkapi.messages

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol


object SparkApiMessages extends SprayJsonSupport with DefaultJsonProtocol {
  type EnvVars = Map[String, String]
  trait SparkApiProtocol

  //Requests
  trait SparkRequest extends SparkApiProtocol
  case class SubmitJob(name: String, arguments: Set[String], jarLocation: String, envVars: EnvVars) extends SparkRequest
  case class JobStatus(driverId: String) extends SparkRequest
  case class KillJob(driverId: String) extends SparkRequest

  //Responses
  trait SparkResponse
  case class SparkResponseContent(action: String, message: String, driverId: String, success: Boolean)
      extends SparkApiProtocol with SparkResponse

  case class Ok(driverId: String) extends SparkResponse
  case class Error(driverId: String) extends SparkResponse

  //Unmarshalling responses
  implicit val responseFormat = jsonFormat4(SparkResponseContent)
}

