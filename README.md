Akka Http Spark Medical
-----------------------
>This project exports an Akka Http REST service that selects diet data from a medical Spark job based on
>patient and encounter id values.

Test
----
1. sbt clean test

Run
---
1. sbt run

Curl
----
1. sbt run
2. curl http://localhost:7676/api/v1/diet/1/1

Package
-------
1. sbt universal:packageZipTarball | windows:packageBin

Run
---
1. tar -xvzf ./target/universal/akka-http-spark-medical-0.1.tgz -C ./target/universal
2. sh ./target/universal/akka-http-spark-medical-0.1/bin/akka-http-spark-medical