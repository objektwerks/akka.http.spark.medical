package medical

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import akka.testkit.TestDuration

import com.typesafe.config.ConfigFactory

import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration._
import scala.language.postfixOps

class RouterTest extends AnyWordSpec with Matchers with ScalatestRouteTest  {
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
  val server = Http().bindAndHandle(router.api, host, port)
  logger.info(s"*** Test Server started at: http://$host:$port")

  import de.heikoseeberger.akkahttpupickle.{UpickleSupport => Upickle}
  import Upickle._
  import upickle.default._

  "getDietById" should {
    "return diet json" in {
      Get(conf.getString("rest.url")) ~> router.api ~> check {
        status shouldBe OK
        val json = responseAs[String]
        logger.info(s"*** ServerTest: getDietById > $json")
        val diets = read[List[Diet]](json)
        diets.nonEmpty shouldBe true
        diets foreach { diet => assert(diet.isValid) }
      }
    }
  }
}