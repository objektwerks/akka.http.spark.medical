package medical

import org.apache.spark.internal.Logging
import org.apache.spark.sql.functions.lower

import scala.concurrent.Future

class SparkJob(sparkInstance: SparkInstance) extends Logging {
  def listDietById(patientId: Long, encounterId: Long): Future[List[Diet]] = {
    import sparkInstance._
    import sparkSession.implicits._

    log.info(s"*** SparkJob: Processing patient id ($patientId) and encounter id ($encounterId)...")

    val diets = List( Diet(patientId = 1, encounterId = 1, status = "Good", diet = "Raw") )
    val dietsDataset = diets.toDS.withColumn("status", lower($"status")).as[Diet]
    val dietsTransformed = dietsDataset.collect.toList
    Future.successful[List[Diet]]( dietsTransformed )
  }
}

object SparkJob {
  def apply(sparkInstance: SparkInstance): SparkJob = new SparkJob(sparkInstance)
}