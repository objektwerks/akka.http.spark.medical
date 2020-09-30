package medical

import org.apache.spark.internal.Logging
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.concurrent.Future
import scala.concurrent.ExecutionContextExecutor

case class SparkJobConf(master: String, name: String)

class SparkJob(conf: SparkJobConf) extends Logging {
  def listDietById(patientId: Long, encounterId: Long)(implicit dispatcher: ExecutionContextExecutor): Future[List[Diet]] = {
    val sparkConf = new SparkConf()
      .setMaster(conf.master)
      .setAppName(conf.name)
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(Array(classOf[Diet]))
    log.info(s"*** SparkJob: SparkConf loaded.")

    val sparkSession = SparkSession
      .builder
      .config(sparkConf)
      .getOrCreate()
    log.info(s"*** SparkJob: SparkSession created.")

    // Spark code here!
    log.info(s"*** SparkJob: Processing patient id ($patientId) and encounter id ($encounterId)...")

    log.info(s"*** SparkJob: Stopping")
    sparkSession.stop

    // Must return a Future of List[Diet]!
    Future( List[Diet]( Diet(1, 2, "Success", "Raw") ) )
  }
}

object SparkJob {
  def apply(conf: SparkJobConf): SparkJob = new SparkJob(conf)
}