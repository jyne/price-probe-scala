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
      .set("spark.cassandra.connection.host", remoteFactory.cassandraSubnet)
      .set("spark.cassandra.connection.port", remoteFactory.cassandraPort.toString)
    val sparkSession = SparkSession
      .builder()
      .appName("price-probe")
      .config(configuration)
      .enableHiveSupport()
      .getOrCreate()

    sparkContext = sparkSession
  }

  def getSparkInstance : SparkSession = {
    sparkContext
  }

}