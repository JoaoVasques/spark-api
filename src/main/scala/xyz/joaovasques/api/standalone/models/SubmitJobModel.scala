package xyz.joaovasques.sparkapi.api.standalone.models

import xyz.joaovasques.sparkapi.messages.SparkApiMessages._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

case class SubmitJobModel(
  action: String,
  appArgs: Set[String],
  appResource: String,
  clientSparkVersion: String,
  environmentVariables: Map[String, String],
  mainClass: String,
  sparkProperties: Map[String, String]
) {
  def toJson() = this.asJson.noSpaces
}

object SubmitJobModel {
  import cats.Semigroup
  import cats.implicits._

  private val defaultAction = "CreateSubmissionRequest"

  private def defaultSparkProperties(jarLocation: String, masterIp: String, deployMode: String = "cluster") =
    Map("spark.jars" -> jarLocation,
      "spark.subtmit.deployModel" -> deployMode,
      "spark.app.name" -> "some-name",
      "spark.master" -> s"spark://${masterIp}:6066"
    )

  private val defaultEnvironmentVariables = Map("SPARK_ENV_LOADED" -> "1")

  def apply(request: SubmitJob, master: String, envVars: Map[String, String]): SubmitJobModel =
    new SubmitJobModel(defaultAction,
      request.arguments,
      request.jarLocation,
      "2.1.0", //clientSparkVersion,
      request.envVars |+| defaultEnvironmentVariables,
      request.mainClass,
      defaultSparkProperties(request.jarLocation, master) |+| request.sparkProperties
    )

}

