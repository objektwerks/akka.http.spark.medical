Akka Http Spark Medical
-----------------------
>This project exports an Akka Http REST service that selects diet data from a medical Spark job based on
>patient and encounter id values.

Design
------
>App create an embedded Akka-Http Server and SparkInstance to be used by all SparkJob instances created
>by Router. See:
1. App --- create ---> Server | SparkInstance
2. App --- run ---> Server.flightCheck
3. Client --- http get request ---> Router --- request ---> SparkJob
4. SparkJob --- response ---> Router --- http response ---> Client

Scala
-----
>Must use Scala 2.11 due to Spark constraints.

Java
----
>Must use Java 8 due to Spark constraints.

Test
----
1. sbt clean test

Run
---
1. sbt run
2. Enter any key to stop app.

Curl
----
1. sbt run
2. curl http://localhost:7676/api/v1/diet/1/1
3. Enter any key to stop app.

Package
-------
1. sbt clean compile universal:packageZipTarball | windows:packageBin

Run
---
1. tar -xvzf ./target/universal/akka-http-spark-medical-0.1.tgz -C ./target/universal
2. sh ./target/universal/akka-http-spark-medical-0.1/bin/akka-http-spark-medical

Submit
------
1. sbt universal:packageZipTarball | windows:packageBin 
2. tar -xvzf ./target/universal/akka-http-spark-medical-0.1.tgz -C ./target/universal
3. chmod +x submit.sh ( required only once )
4. ./submit.sh