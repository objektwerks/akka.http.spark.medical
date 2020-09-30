package medical

case class Diet(patientId: Long, encounterId: Long, status: String, diet: String) extends Product with Serializable {
  def isValid: Boolean = patientId > 0 && encounterId > 0 && status.nonEmpty && diet.nonEmpty
}

object Diet {
  import upickle.default._

  implicit val dietRW: ReadWriter[Diet] = macroRW
}