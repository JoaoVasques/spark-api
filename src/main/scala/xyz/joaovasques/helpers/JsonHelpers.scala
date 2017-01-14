package xyz.joaovasques.sparkapi.helpers

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import xyz.joaovasques.sparkapi.messages.SparkApiMessages.SubmitJob

trait JsonHelpers {
  def generateSubmitJsonBody(request: SubmitJob, master: String): String = {
    compact(render(
      ("action" -> "CreateSubmissionRequest") ~
        ("appArgs" -> request.arguments) ~
        ("appResource" -> request.jarLocation) ~
        ("clientSparkVersion" -> "1.6,1") ~ //TODO Change
        ("environmentVariables" -> ("SPARK_ENV_LOADED" -> 1)) ~ //TODO add envars on the request
        ("mainClass" -> request.mainClass) ~
        ("sparkProperties" ->
          ("spark.jars" -> request.jarLocation) ~
          ("spark.submit.deployMode" -> "cluster") ~
          ("spark.app.name" -> "some-name") ~
          ("spark.master" -> s"spark://${master}:6066")
          //TODO add spark streaming graceful shutdown
        )))
  }
}

