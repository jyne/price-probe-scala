name := "price-probe-scala"

version := "1.0"

scalaVersion := "2.11.6"

// https://mvnrepository.com/artifact/org.scalatest/scalatest_2.11
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.5" % "test"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http_2.11
libraryDependencies += "com.typesafe.akka" % "akka-http_2.11" % "10.0.7"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http-testkit_2.11
libraryDependencies += "com.typesafe.akka" % "akka-http-testkit_2.11" % "10.0.7" % "test"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http-spray-json-experimental_2.11
libraryDependencies += "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.11.2"
// https://mvnrepository.com/artifact/ch.megard/akka-http-cors_2.11
libraryDependencies += "ch.megard" % "akka-http-cors_2.11" % "0.2.1"
// https://mvnrepository.com/artifact/log4j/log4j
libraryDependencies += "log4j" % "log4j" % "1.2.17"
// https://mvnrepository.com/artifact/com.datastax.cassandra/cassandra-driver-core
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.3.0"
// https://mvnrepository.com/artifact/commons-codec/commons-codec
libraryDependencies += "commons-codec" % "commons-codec" % "1.9"
// https://mvnrepository.com/artifact/commons-beanutils/commons-beanutils
libraryDependencies += "commons-beanutils" % "commons-beanutils" % "1.9.3"
libraryDependencies += "com.typesafe" % "config" % "1.3.1"
// https://mvnrepository.com/artifact/io.monix/monix_2.11
libraryDependencies += "io.monix" % "monix_2.11" % "3.0.0-22bf9c6"
// https://mvnrepository.com/artifact/joda-time/joda-time
libraryDependencies += "joda-time" % "joda-time" % "2.3"
// https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"

enablePlugins(JavaAppPackaging)