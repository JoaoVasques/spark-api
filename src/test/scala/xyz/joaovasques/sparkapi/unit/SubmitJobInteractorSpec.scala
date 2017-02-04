package xyz.joaovasques.sparkapi.tests.unit

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest._
import org.scalatest.concurrent._
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.AbstractPatienceConfiguration
import org.scalatest.time._
import xyz.joaovasques.sparkapi.api.SubmitJobInteractor
import xyz.joaovasques.sparkapi.messages.SparkApiMessages._
import xyz.joaovasques.sparkapi.tests.helpers._
import scala.concurrent._, duration._

class SubmitJobInteractorSpec extends TestKit(ActorSystem("KillJobInteractorSpec"))
    with InteractorHelpers
    with JobHelpers
    with FunSpecLike
    with ScalaFutures
    with RecoverMethods
    with Matchers
    with BeforeAndAfterAll {

  private val validJob = SubmitJob("Hello World", "com.example.TestBatchJob" ,Set(), dummyJobJarLocation, Map())

  private val interactor = new SubmitJobInteractor(apiRequest, masterIp)

  override def afterAll() = TestKit.shutdownActorSystem(system)

  describe("a SubmitJobInteractor") {
    describe("when having an existing job") {
      it("should be able to submit it") {
        val future = interactor.call(validJob).map(_.success)
        whenReady(future, Timeout(Span(5, Seconds))) { _ should be (true) }
      }
    }
  }
}

