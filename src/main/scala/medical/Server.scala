package medical

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.{ConnectionContext, Http}

import com.typesafe.config.Config

import scala.concurrent.Future

case class ServerConf(name: String, host: String, port: Int, service: String) {
  def tuple: (String, String, Int, String) = (name, host, port, service)
}

object Server {
  def apply(conf: Config,
            serverConf: ServerConf,
            sslContextConf: SSLContextConf,
            sparkInstance: SparkInstance): Server = new Server(conf, serverConf, sslContextConf, sparkInstance)
}

class Server(conf: Config,
             serverConf: ServerConf,
             sslContextConf: SSLContextConf,
             sparkInstance: SparkInstance) {
  private val (name, host, port, service) = serverConf.tuple

  private implicit val system = ActorSystem.create(name, conf)
  private implicit val dispatcher = system.dispatcher
  private val logger = system.log

  private val router = Router(sparkInstance, logger)
  private val sslContext = SSLContextFactory.newInstance(sslContextConf)
  private val httpsContext = ConnectionContext.https(sslContext)
  Http().setDefaultServerHttpContext(httpsContext)
  private val server = Http().bindAndHandle(router.api, host, port)
  logger.info(s"*** SSL context conf: ${sslContextConf.toString}")
  logger.info(s"*** Server started at https://$host:$port/\nPress RETURN to stop...")

  def flightCheck: Future[Boolean] = {
    val client = Http()
    client.setDefaultServerHttpContext(httpsContext)
    client.singleRequest( HttpRequest(uri = s"https://$host:$port/$service") ).map { response =>
      response.entity.dataBytes.map(_.utf8String)
        .runForeach( diet => logger.info(s"*** Flight Check [Success]: $diet") )
      true
    }.recover { case failure =>
      logger.error(s"*** Flight Check [Failure]: ${failure.getMessage}")
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