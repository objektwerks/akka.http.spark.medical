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

    server.preFlightCheck.map( clearedToFly => require(clearedToFly, "*** PreFlight Check failed! You are not cleared to fly!") )

    StdIn.readLine()
    sparkInstance.shutdown()
    server.shutdown()
  }
}