package medical

import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession

case class SparkInstanceConf(master: String, name: String)

object SparkInstance {
  def apply(conf: SparkInstanceConf): SparkInstance = new SparkInstance(conf)
}

class SparkInstance(val conf: SparkInstanceConf) extends Logging {
  private val sparkConf = new SparkConf()
    .setMaster(conf.master)
    .setAppName(conf.name)
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .registerKryoClasses(Array(classOf[Diet]))
  log.info(s"*** SparkInstance: SparkConf loaded.")

  val sparkSession = SparkSession
    .builder
    .config(sparkConf)
    .enableHiveSupport
    .getOrCreate()
  log.info(s"*** SparkInstance: SparkSession created.")

  val sparkContext = sparkSession.sparkContext
  log.info(s"*** SparkInstance: SparkContext created.")

  def shutdown(): Unit = {
    if ( !sparkContext.isStopped ) sparkSession.stop
    log.info(s"*** SparkInstance: SparkSession stopped.")
  }
}