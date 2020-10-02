package medical

import org.apache.spark.internal.Logging

import scala.concurrent.Future

class SparkJob(sparkInstance: SparkInstance) extends Logging {
  def listDietById(patientId: Long, encounterId: Long): Future[List[Diet]] = {
    import sparkInstance._

    // Spark code here!
    log.info(s"*** SparkJob: Processing patient id ($patientId) and encounter id ($encounterId)...")

    // Must return a Future.successful(List[Diet])!
    Future.successful[List[Diet]]( List[Diet]( Diet(1, 2, "Success", "Raw") ) )
  }
}

object SparkJob {
  def apply(sparkInstance: SparkInstance): SparkJob = new SparkJob(sparkInstance)
}