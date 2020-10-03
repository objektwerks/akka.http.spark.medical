package medical

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.event.LoggingAdapter

import de.heikoseeberger.akkahttpupickle.UpickleSupport._

import scala.util.{Failure, Success}

import upickle.default._

object Router {
  def apply(sparkInstance: SparkInstance, logger: LoggingAdapter): Router = new Router(sparkInstance, logger)
}

class Router(val sparkInstance: SparkInstance, logger: LoggingAdapter) {
  private val listDietsByIds = path(LongNumber / LongNumber) { (patientId, encounterId) =>
    logger.info(s"*** Router: listDietsByIds / patientId: $patientId / encounterId: $encounterId")
    onComplete( SparkJob(sparkInstance).listDietsByIds(patientId, encounterId) ) {
      case Success(diets) => complete(OK -> write[List[Diet]](diets))
      case Failure(error) =>
        val message = error.getMessage
        logger.error(s"*** Router: listDietsByIds > $message")
        complete(BadRequest -> message)
    }
  }
  val api = pathPrefix("api" / "v1" / "diet") {
    listDietsByIds
  }
}