Akka Http Medical
-----------------
>This project exports an Akka Http REST service that selects diet data from a medical Spark job based on
>patient and encounter id values.

Https
-----
>For details see:
1. https://doc.akka.io/docs/akka-http/current/server-side/server-https-support.html
2. https://lightbend.github.io/ssl-config/CertificateGeneration.html
>Also see:
1. x509.txt
2. x509 directory

SSL
---
>Both JKS and PKCS12 keystore formats have been tested to date. Simply edited the server.conf accordingly.
1. passphrase="password", keystorePath="./x509/localhost.jks", keystoreType="JKS", sslProtocol="TLS", algorithm="SunX509"
2. passphrase="password", keystorePath="./x509/localhost.p12", keystoreType="PKCS12", sslProtocol="TLS", algorithm="SunX509"
>See the x509.txt file, section [ Create PKCS12 Keystore ], for how to create the localhost.p12 keystore.    

Test
----
1. sbt clean test

Run
---
1. sbt run

Run Extended
------------
>Run with optional system property args. See server.conf for **all** system properties with a ${?property} format.
1. sbt "run -Dconfig.override_with_env_vars=true -Dhost=127.0.0.1 -Dport=7443"

Curl
----
>Run app and query rest service:
1. curl https://localhost:7443/api/v1/diet/1/1

Package
-------
>Using https://sbt-native-packager.readthedocs.io/en/stable/
1. sbt universal:packageZipTarball | windows:packageBin

Run
---
1. tar -xvzf ./target/universal/akka-http-spark-medical-0.1.tgz -C ./target/universal
2. sh ./target/universal/akka-http-spark-medical-0.1/bin/akka-http-spark-medical