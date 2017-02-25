package xyz.joaovasques.sparkapi.tests.unit

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest._
import xyz.joaovasques.sparkapi.tests.helpers.InteractorHelpers
import xyz.joaovasques.sparkapi.api.standalone.interactors._
import xyz.joaovasques.sparkapi.actors.SparkActor._

class KillJobInteractorSpec extends TestKit(ActorSystem("KillJobInteractorSpec"))
    with InteractorHelpers
    with FunSpecLike
    with RecoverMethods
    with Matchers
    with BeforeAndAfterAll {

  private val neverEndingJob = SubmitJob("name", "main-class" ,Set(), "jar", Map())
  private val interactor = new KillJobInteractor(apiRequest, masterIp)

  override def afterAll() = TestKit.shutdownActorSystem(system)

  describe("a KillJobInteractor") {
    describe("when having a running job") {
      it("should be able to kill it") {
        pending
      }
    }

    describe("when having a non running job") {
      it("shouldn't be able to kill it") {
        recoverToExceptionIf[Exception] { interactor.call("invalidJob") }
      }
    }
  }
}

