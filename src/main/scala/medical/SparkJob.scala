package medical

import org.apache.spark.internal.Logging
import org.apache.spark.sql.functions.lower

import scala.concurrent.Future

final case class SparkJob(sparkInstance: SparkInstance) extends Logging with Product with Serializable {
  def listDietByIds(patientId: Long, encounterId: Long): Future[List[Diet]] = {
    import sparkInstance._
    import sparkSession.implicits._

    log.info(s"*** SparkJob: Processing patient id ($patientId) and encounter id ($encounterId)...")

    val diet = List( Diet(patientId = 1, encounterId = 1, status = "Good", diet = "Raw") )
    val dietDataset = diet.toDS
      .withColumn("status", lower($"status"))
      .withColumn("diet", lower($"diet"))
      .as[Diet]
    val dietTransformed = dietDataset.collect.toList
    Future.successful[List[Diet]]( dietTransformed )
  }
}