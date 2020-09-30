package medical

import akka.actor.ActorSystem
import akka.http.scaladsl.{ConnectionContext, Http}
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import scala.io.StdIn

case class ServerConf(name: String, host: String, port: Int) {
  def tuple: (String, String, Int) = (name, host, port)
}

object Server {
  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load("server.conf")
    val (name, host, port) = conf.as[ServerConf]("server").tuple
    val sslContextConf = conf.as[SSLContextConf]("ssl")

    implicit val system = ActorSystem.create(name, conf)
    implicit val dispatcher = system.dispatcher
    val logger = system.log

    val store = Store(conf)
    val router = Router(store)
    val sslContext = SSLContextFactory.newInstance(sslContextConf)
    val httpsContext = ConnectionContext.httpsServer(sslContext)
    val server = Http()
      .newServerAt(host, port)
      .enableHttps(httpsContext)
      .bindFlow(router.api)

    logger.info(s"*** SSL context conf: ${sslContextConf.toString}")
    logger.info(s"*** Server started at https://$host:$port/\nPress RETURN to stop...")

    StdIn.readLine()
    server
      .flatMap(_.unbind)
      .onComplete { _ =>
        system.terminate
        logger.info("Server stopped.")
      }
  }
}