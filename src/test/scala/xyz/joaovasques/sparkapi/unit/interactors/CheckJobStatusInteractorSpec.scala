package xyz.joaovasques.sparkapi.tests.unit

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import xyz.joaovasques.sparkapi.tests.helpers.InteractorHelpers
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time._
import xyz.joaovasques.sparkapi.api.standalone.interactors._
import xyz.joaovasques.sparkapi.actors.SparkActor._

class CheckJobStatusInteractorSpec extends TestKit(ActorSystem("SubmitJobInteractorSpec"))
    with InteractorHelpers
    with FunSpecLike
    with RecoverMethods
    with ScalaFutures
    with Matchers
    with BeforeAndAfterAll {

  private val validJob = SubmitJob("name", "main-class" ,Set(), "jar", Map())
  private val invalidJob = SubmitJob("invalid-name", "main-class", Set(), "no-jar", Map())

  private val submitInteractor = new SubmitJobInteractor(apiRequest, masterIp)
  private val checkStatusInteractor = new CheckJobStatusInteractor(apiRequest, masterIp)

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
      it("should be able to get it's it's status as ERROR") {
        val submissionIdFuture = submitInteractor.call(invalidJob).map(_.submissionId)

        Thread.sleep(5000)

        val futureResult = for {
          submissionId <- submissionIdFuture
          status <- checkStatusInteractor.call(submissionId)
        } yield status.driverState

        whenReady(futureResult, Timeout(Span(5, Seconds))) { _ should be ("ERROR") }
      }
    }
  }
}

