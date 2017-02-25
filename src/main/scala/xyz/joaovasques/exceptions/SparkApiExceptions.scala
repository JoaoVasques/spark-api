package xyz.joaovasques.sparkapi.exceptions

object SparkApiExceptions {

  case class SparkCommunicationException(statusCode: Int) extends Exception()
  case class JobSubmissionFailedException() extends Exception
}

