package xyz.joaovasques.sparkapi.helpers

import xyz.joaovasques.sparkapi.api.standalone.models._
import xyz.joaovasques.sparkapi.actors.SparkActor.SubmitJob

trait JsonHelpers {

  def generateSubmitJsonBody(request: SubmitJob, master: String, envVars: Map[String, String]): String =
    SubmitJobModel(request, master, envVars).toJson
}

