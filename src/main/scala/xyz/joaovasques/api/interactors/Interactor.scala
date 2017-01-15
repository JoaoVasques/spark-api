package xyz.joaovasques.sparkapi.api

import scala.concurrent.Future
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.HttpHeader.ParsingResult
trait Interactor[I,O] {

  val sparkApi: HttpRequest => Future[HttpResponse]
  val apiVersion: String
  val master: String

  protected val header =
    HttpHeader.parse("Content-Type", "application/json;charset=UTF-8") match {
      case ParsingResult.Ok(header, _)=> header
      case _ => ???
    }

  def call(request: I): Future[O]
}

