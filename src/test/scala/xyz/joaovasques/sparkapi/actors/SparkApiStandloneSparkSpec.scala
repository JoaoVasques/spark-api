package xyz.joaovasques.sparkapi.actors

import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ ImplicitSender, TestKit }
import org.scalatest._
import xyz.joaovasques.sparkapi.messages.SparkApiMessages._

class SparkApiStandloneSpec extends TestKit(ActorSystem("MySpec")) with ImplicitSender
    with FunSpecLike with Matchers {

  private val masterIp = "127.0.0.1"
  private val sparkActor = system.actorOf(Props(new SparkActor("master", SparkApiStandlone(masterIp))), "spark-actor-test")
  private val validJob = SubmitJob("name", Set(), "jar", Map())
  private val invalidJob = SubmitJob("invalid-name", Set(), "no-jar", Map())

  describe("SparkApi for Standlone Spark Cluster") {
    describe("when having an existing job") {
      it("should be able to submit it") {
        // sparkActor ! validJob
        // expectMsgType[Ok]
        pending
      }

      it("should be able to check it's status") {
        sparkActor ! JobStatus("xpto")
        expectMsgType[Ok]
      }

      it("should be able to kill it") {
        pending
      }
    }

    describe("when having a non existing job") {
      it("shouldn't be able to submit it") {
        // sparkActor ! invalidJob
        // expectMsgType[Error]
        pending
      }

      it("shouldn't be able to kill it") {
        // sparkActor ! KillJob("id")
        // expectMsgType[Error]
        pending
      }

      it("shouldn't be able to check its status") {
        // sparkActor ! JobStatus("id")
        // expectMsgType[Error]
        pending
      }
    }
  }
}

