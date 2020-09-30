enablePlugins(JavaAppPackaging)

name := "akka.http.spark.medical"
organization := "objektwerks"
version := "0.1"
scalaVersion := "2.13.2"
libraryDependencies ++= {
  val akkaVersion = "2.6.8"
  val akkkHttpVersion = "10.2.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkkHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "de.heikoseeberger" %% "akka-http-upickle" % "1.32.0",
    "org.scalikejdbc" %% "scalikejdbc" % "3.4.2",
    "com.oracle.ojdbc" % "ojdbc8" % "19.3.0.0",
    "com.lihaoyi" %% "upickle" % "1.1.0",
    "com.typesafe" % "config" % "1.4.0",
    "com.iheart" %% "ficus" % "1.4.7",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % akkkHttpVersion % Test,
    "org.scalatest" %% "scalatest" % "3.2.0" % Test
  )
}