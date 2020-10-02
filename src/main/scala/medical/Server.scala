package medical

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.Http

import com.typesafe.config.Config

import scala.concurrent.Future

case class ServerConf(name: String, host: String, port: Int, service: String) {
  def tuple: (String, String, Int, String) = (name, host, port, service)
}

object Server {
  def apply(conf: Config,
            serverConf: ServerConf,
            sparkInstance: SparkInstance): Server = new Server(conf, serverConf, sparkInstance)
}

class Server(conf: Config,
             serverConf: ServerConf,
             sparkInstance: SparkInstance) {
  private val (name, host, port, service) = serverConf.tuple

  private implicit val system = ActorSystem.create(name, conf)
  private implicit val dispatcher = system.dispatcher
  private val logger = system.log

  private val router = Router(sparkInstance, logger)
  private val server = Http().bindAndHandle(router.api, host, port)
  logger.info(s"*** Server started at http://$host:$port/\nPress RETURN to stop...")

  def preFlightCheck: Future[Boolean] = {
    val client = Http()
    client.singleRequest( HttpRequest(uri = s"http://$host:$port/$service") ).map { response =>
      response.entity.dataBytes.map(_.utf8String)
        .runForeach( diet => logger.info(s"*** PreFlight Check [Success]: $diet") )
      true
    }.recover { case failure =>
      logger.error(s"*** PreFlight Check [Failure]: ${failure.getMessage}")
      false
    }
  }

  def shutdown(): Unit = {
    server
      .flatMap(_.unbind)
      .onComplete { _ => system.terminate }
    logger.info(s"*** Server at https://$host:$port/ stopped.")
  }
}