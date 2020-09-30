package medical

import com.typesafe.config.Config
import org.slf4j.LoggerFactory
import scalikejdbc.{ConnectionPool, DB, scalikejdbcSQLInterpolationImplicitDef}

import scala.concurrent.Future

class Store(conf: Config) {
  private val logger = LoggerFactory.getLogger(getClass)
  private val driver = conf.getString("db.driver")
  private val url = conf.getString("db.url")
  private val user = conf.getString("db.user")
  private val password = conf.getString("db.password")

  Class.forName(driver)
  ConnectionPool.singleton(url, user, password)
  logger.info(s"*** Store: Connected to Oracle store ( $url ).")

  def listDietById(patientId: Long, encounterId: Long): Future[List[Diet]] = DB readOnly { implicit session =>
    val result = sql"""
                   select PERSON_ID as Patient,
                   ENCNTR_ID as Encounter,
                   pi_get_cv_display(ORDER_STATUS_CD) as Status,
                   ORDER_MNEMONIC || ' ' || CLINICAL_DISPLAY_LINE as Diet
      from V500.ORDERS
      where ACTIVITY_TYPE_CD = 681598
      and CATALOG_TYPE_CD = 2511
      and PERSON_ID = $patientId
      and ENCNTR_ID = $encounterId
      and ACTIVE_IND = 1
      """
      .map(rs => Diet(rs.long("Patient"), rs.long("Encounter"), rs.string("Status"), rs.string("Diet")))
      .list
      .apply
    Future.successful(result)
  }
}

object Store {
  def apply(conf: Config): Store = new Store(conf)
}