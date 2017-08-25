package priceprobe.connection

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf}

/**
  * Created by andream16 on 18.07.17.
  */
class SparkConnectionFactory {

  val remoteFactory = new RemoteConnectionFactory
  var sparkContext : SparkSession = _

  def initSparkConnection() : Unit = {
    val configuration = new SparkConf()
      .setAppName("price-probe")
      .setMaster("spark://" + remoteFactory.sparkSubnet + ":" + remoteFactory.sparkPort)
      //.setMaster("local[*]")
      //.set("spark.executor.host", remoteFactory.remoteHost)
      //.set("spark.executor.port", remoteFactory.sparkPort.toString)
      .set("spark.cassandra.connection.host", remoteFactory.cassandraSubnet)
      .set("spark.cassandra.connection.port", remoteFactory.cassandraPort.toString)
    val sparkSession = SparkSession
      .builder()
      .appName("price-probe")
      .config(configuration)
      .enableHiveSupport()
      .getOrCreate()

    sparkContext = sparkSession


    sparkContext.sparkContext.addJar("http://central.maven.org/maven2/com/datastax/spark/spark-cassandra-connector_2.11/2.0.3/spark-cassandra-connector_2.11-2.0.3.jar")
  }

  def getSparkInstance : SparkSession = {
    sparkContext
  }

}
