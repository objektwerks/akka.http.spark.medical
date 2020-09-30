package medical

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpupickle.UpickleSupport._
import org.slf4j.LoggerFactory
import upickle.default._

import scala.util.{Failure, Success}

class Router(store: Store) {
  val logger = LoggerFactory.getLogger(getClass)

  val getDietById = path(LongNumber / LongNumber) { (patientId, encounterId) =>
    logger.debug(s"*** Router: getDietById / patientId: $patientId / encounterId: $encounterId")
    onComplete(store.listDietById(patientId, encounterId)) {
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
  def apply(store: Store): Router = new Router(store)
}