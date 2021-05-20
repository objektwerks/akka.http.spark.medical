package medical

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import akka.testkit.TestDuration

import com.typesafe.config.ConfigFactory

import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.Await

class RouterTest extends AnyWordSpec with BeforeAndAfterAll with Matchers with ScalatestRouteTest  {
  val conf = ConfigFactory.load("router.conf")
  val sparkInstanceConf = conf.as[SparkInstanceConf]("spark")

  val sparkInstance = SparkInstance( sparkInstanceConf )

  val actorRefFactory = ActorSystem.create(conf.getString("server.name"), conf.getConfig("akka"))
  implicit val dispatcher = system.dispatcher
  implicit val timeout = RouteTestTimeout(10.seconds dilated)
  val logger = actorRefFactory.log

  val router = Router(sparkInstance, logger)
  val host = conf.getString("server.host")
  val port = conf.getInt("server.port")
  val server = Http()
    .newServerAt(host, port)
    .bindFlow(router.api)
    .map { server =>
      logger.info(s"*** Server started at: ${server.localAddress.toString}")
      server
    }

  override protected def afterAll(): Unit =
    server
      .flatMap(_.unbind())
      .onComplete { _ =>
        logger.info("*** Server shutting down...")
        actorRefFactory.terminate()
        Await.result(actorRefFactory.whenTerminated, 3.seconds)
        logger.info("*** Server shutdown.")
      } 

  import de.heikoseeberger.akkahttpupickle.{UpickleSupport => Upickle}
  import Upickle._
  import upickle.default._

  "listDietsByIds" should {
    "return diets json" in {
      Get(conf.getString("rest.url")) ~> router.api ~> check {
        status shouldBe OK
        val json = responseAs[String]
        logger.info(s"*** RouterTest: listDietsByIds > $json")
        val diets = read[List[Diet]](json)
        diets.nonEmpty shouldBe true
        diets foreach { diet => assert(diet.isValid) }
      }
    }
  }
}