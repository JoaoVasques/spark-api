package xyz.joaovasques.sparkapi.tests.unit

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest._
import xyz.joaovasques.sparkapi.api.SubmitJobInteractor
import xyz.joaovasques.sparkapi.messages.SparkApiMessages._
import xyz.joaovasques.sparkapi.tests.helpers.InteractorHelpers

class CheckJobStatusInteractorSpec extends TestKit(ActorSystem("SubmitJobInteractorSpec"))
    with InteractorHelpers
    with FunSpecLike
    with RecoverMethods
    with Matchers
    with BeforeAndAfterAll {

  private val validJob = SubmitJob("name", "main-class" ,Set(), "jar", Map())
  private val invalidJob = SubmitJob("invalid-name", "main-class", Set(), "no-jar", Map())

  private val interactor = new SubmitJobInteractor(apiRequest, masterIp)

  override def afterAll() = TestKit.shutdownActorSystem(system)

  describe("a CheckJobStatusInteractor") {
    describe("when having a running job") {
      it("should be able to get it's it's status as RUNNING") {
        pending
      }
    }

    describe("when having a job that ended") {
      it("should be able to get it's it's status as FINISHED") {
        pending
      }
    }

    describe("when having a job that failed") {
      it("should be able to get it's it's status as FAILED") {
        pending
      }
    }
  }
}

