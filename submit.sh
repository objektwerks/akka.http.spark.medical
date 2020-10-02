#!/bin/sh
spark-submit \
  --class medical.App \
  --master local[2] \
  ./target/universal/akka-http-spark-medical-0.1/bin/akka-http-spark-medical