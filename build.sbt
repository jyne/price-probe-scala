name := "price-probe-scala"

version := "1.0"

scalaVersion := "2.11.8"

// https://mvnrepository.com/artifact/org.scalatest/scalatest_2.11
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.5" % "test"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http_2.11
libraryDependencies += "com.typesafe.akka" % "akka-http_2.11" % "10.0.7"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http-testkit_2.11
libraryDependencies += "com.typesafe.akka" % "akka-http-testkit_2.11" % "10.0.7" % "test"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http-spray-json-experimental_2.11
libraryDependencies += "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.11.2"
// https://mvnrepository.com/artifact/org.slf4j/slf4j-api
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25"
// https://mvnrepository.com/artifact/org.slf4j/jcl-over-slf4j
libraryDependencies += "org.slf4j" % "jcl-over-slf4j" % "1.7.25"
// https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25" % "test"
// https://mvnrepository.com/artifact/com.jcraft/jsch
libraryDependencies += "com.jcraft" % "jsch" % "0.1.54"
// https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.11
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.1.1"
// https://mvnrepository.com/artifact/com.datastax.spark/spark-cassandra-connector_2.11
libraryDependencies += "com.datastax.spark" % "spark-cassandra-connector_2.11" % "2.0.3"
// https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.11
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.1.1" % "provided"
// https://mvnrepository.com/artifact/org.apache.spark/spark-hive_2.10
libraryDependencies += "org.apache.spark" % "spark-hive_2.11" % "2.1.1"
// https://mvnrepository.com/artifact/commons-codec/commons-codec
libraryDependencies += "commons-codec" % "commons-codec" % "1.9"
// https://mvnrepository.com/artifact/ch.megard/akka-http-cors_2.11
libraryDependencies += "ch.megard" % "akka-http-cors_2.11" % "0.2.1"
// https://mvnrepository.com/artifact/stax/stax-api
libraryDependencies += "stax" % "stax-api" % "1.0.1"
// https://mvnrepository.com/artifact/commons-beanutils/commons-beanutils
libraryDependencies += "commons-beanutils" % "commons-beanutils" % "1.9.3"

enablePlugins(JavaAppPackaging)