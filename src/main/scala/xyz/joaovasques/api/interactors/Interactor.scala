package xyz.joaovasques.sparkapi.api

import scala.concurrent.Future
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

trait Interactor[I,O] {

  val sparkApi: HttpRequest => Future[HttpResponse]
  val apiVersion: String
  val master: String

  def call(request: I): Future[O]
}

