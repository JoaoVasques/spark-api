package xyz.joaovasques.sparkapi

import xyz.joaovasques.sparkapi.actors._
import akka.actor.{ Actor, ActorSystem, Props, ActorRef }
import xyz.joaovasques.sparkapi.api.standalone._

object SparkApi {

  def getStandaloneGateway(sparkMaster: String)(implicit system: ActorSystem): ActorRef = {
    system.actorOf(Props(new SparkActor(sparkMaster, SparkApiStandlone(sparkMaster))))
  }
}

