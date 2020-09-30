package medical

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.event.LoggingAdapter

import com.typesafe.config.Config

import de.heikoseeberger.akkahttpupickle.UpickleSupport._

import upickle.default._

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContextExecutor

class Router(val conf: Config, logger: LoggingAdapter)(implicit dispatcher: ExecutionContextExecutor) {
  val getDietById = path(LongNumber / LongNumber) { (patientId, encounterId) =>
    logger.debug(s"*** Router: getDietById / patientId: $patientId / encounterId: $encounterId")
    onComplete(SparkJob(conf, logger).listDietById(patientId, encounterId)) {
      case Success(diets) => complete(OK -> write[List[Diet]](diets))
      case Failure(error) =>
        val message = error.getMessage
        logger.error(s"*** Router: getDietById > $message")
        complete(BadRequest -> message)
    }
  }
  val api = pathPrefix("api" / "v1" / "diet") {
    getDietById
  }
}

object Router {
  def apply(conf: Config, logger: LoggingAdapter)(implicit dispatcher: ExecutionContextExecutor): Router = new Router(conf, logger)
}