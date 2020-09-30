package medical

import akka.event.LoggingAdapter

import com.typesafe.config.Config

import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.concurrent.Future
import scala.concurrent.ExecutionContextExecutor

case class SparkJobConf(master: String, name: String)

class SparkJob(conf: Config, logger: LoggingAdapter) {
  val sparkJobConf = conf.as[SparkJobConf]("spark")

  def listDietById(patientId: Long, encounterId: Long)(implicit dispatcher: ExecutionContextExecutor): Future[List[Diet]] = {
    val sparkConf = new SparkConf()
      .setMaster(sparkJobConf.master)
      .setAppName(sparkJobConf.name)
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(Array(classOf[Diet]))
    logger.info(s"*** SparkJob: SparkConf loaded.")

    val sparkSession = SparkSession
      .builder
      .config(sparkConf)
      .getOrCreate()
    logger.info(s"*** SparkJob: SparkSession created.")

    // Spark code here!
    logger.info(s"*** SparkJob: Processing patient id ($patientId) and encounter id ($encounterId)...")

    logger.info(s"*** SparkJob: Stopping")
    sparkSession.stop

    // Must return a Future of List[Diet]!
    Future( List[Diet]( Diet(1, 2, "Success", "Raw") ) )
  }
}

object SparkJob {
  def apply(conf: Config, logger: LoggingAdapter): SparkJob = new SparkJob(conf, logger)
}