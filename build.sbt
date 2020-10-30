enablePlugins(JavaAppPackaging)

name := "akka.http.spark.medical"
organization := "objektwerks"
version := "0.1"
scalaVersion := "2.12.12"
libraryDependencies ++= {
  val akkaVersion = "2.6.10"
  val akkkHttpVersion = "10.2.1"
  val sparkVersion = "3.0.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkkHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "de.heikoseeberger" %% "akka-http-upickle" % "1.35.2",
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "org.apache.spark" %% "spark-hive" % sparkVersion,
    "com.lihaoyi" %% "upickle" % "1.2.2",
    "com.typesafe" % "config" % "1.4.0",
    "com.iheart" %% "ficus" % "1.5.0",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % akkkHttpVersion % Test,
    "org.scalatest" %% "scalatest" % "3.2.2" % Test
  )
}
