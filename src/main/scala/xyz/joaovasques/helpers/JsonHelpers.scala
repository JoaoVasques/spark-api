package xyz.joaovasques.sparkapi.helpers

import xyz.joaovasques.sparkapi.messages.SparkApiMessages._
import xyz.joaovasques.sparkapi.api.standalone.models._

trait JsonHelpers {

  def generateSubmitJsonBody(request: SubmitJob, master: String, envVars: Map[String, String]): String =
    SubmitJobModel(request, master, envVars).toJson
}

