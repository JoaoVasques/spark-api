package xyz.joaovasques.sparkapi.tests.helpers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Flow, Sink, Source }
import scala.concurrent.Future

trait InteractorHelpers {

  implicit val system: ActorSystem
  implicit val ec = system.dispatcher
  protected val masterIp = "127.0.0.1"
  
  protected val port = 6066
  protected implicit val materializer = ActorMaterializer()

  protected lazy val connectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http().outgoingConnection(masterIp, port)

  protected def apiRequest(request: HttpRequest): Future[HttpResponse] =
    Source.single(request).via(connectionFlow).runWith(Sink.head)
}

