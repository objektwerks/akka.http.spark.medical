package medical

import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

object App {
  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load("app.conf")
    val serverConf = conf.as[ServerConf]("server")
    val sparkInstanceConf = conf.as[SparkInstanceConf]("spark")

    val sparkInstance = SparkInstance(sparkInstanceConf)
    val server = Server(conf, serverConf, sparkInstance)

    server.flightCheck.map( result => require(result, "*** Flight Check failed!") )

    StdIn.readLine()
    sparkInstance.shutdown()
    server.shutdown()
  }
}