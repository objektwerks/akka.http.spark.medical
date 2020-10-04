Akka Http Spark Medical
-----------------------
>This project contains an app that exports an embedded Akka Http REST
>service, which allows for the querying of diet data via a Spark job.

Design
------
>Sequence scenario:
1. App --- create ---> SparkInstance | Server | Router
2. App --- check ---> Server | Router | SparkJob | SparkInstance
3. Client --- http request ---> Router 
4. Router --- create ---> SparkJob
5. Router --- request ---> SparkJob
6. SparkJob --- response ---> Router
7. Router --- http response ---> Client

Scala
-----
>**Must** use Scala 2.11 due to Spark constraints.

Java
----
>**Must** use Java 8 due to Spark constraints.

Logging
-------
>**Must** use Log4j due to a Spark constraint.

Compile
-------
1. sbt clean compile

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

Package and Run
---------------
1. sbt clean compile universal:packageZipTarball | windows:packageBin
2. tar -xvzf ./target/universal/akka-http-spark-medical-0.1.tgz -C ./target/universal
3. sh ./target/universal/akka-http-spark-medical-0.1/bin/akka-http-spark-medical

Spark Submit
------------
1. sbt clean compile package
2. chmod +x submit.sh ( required only once )
3. ./submit.sh