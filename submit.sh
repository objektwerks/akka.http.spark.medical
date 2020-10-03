#!/bin/sh
spark-submit \
  --class medical.App \
  --master local[2] \
  --packages com.typesafe:config:1.3.4 \
             com.iheart:ficus:1.5.0 \
             com.lihaoyi:upickle:0.7.4 \
             de.heikoseeberger:akka-http-upickle:1.28.0 \
             com.typesafe.akka:akka-actor:2.5.31 \
             com.typesafe.akka:akka-slf4j:2.5.31 \
             com.typesafe.akka:akka-http:10.1.12 \
             com.typesafe.akka:akka-stream:2.5.31 \
  ./target/scala-2.11/akka-http-spark-medical_2.11-0.1.jar