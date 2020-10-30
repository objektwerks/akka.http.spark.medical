#!/bin/sh
spark-submit \
  --class medical.App \
  --master local[2] \
  --packages com.typesafe:config:1.4.0,com.iheart:ficus_2.12:1.5.0,com.lihaoyi:upickle_2.12:1.2.2,de.heikoseeberger:akka-http-upickle_2.12:1.35.2,com.typesafe.akka:akka-actor_2.12:2.6.10,com.typesafe.akka:akka-slf4j_2.12:2.6.10,com.typesafe.akka:akka-http_2.12:10.2.1,com.typesafe.akka:akka-stream_2.12:2.6.10 \
  ./target/scala-2.12/akka-http-spark-medical_2.12-${VERSION}.jar