package priceprobe.connection

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by andream16 on 18.07.17.
  */
class SparkConnectionFactory {

  val remoteFactory = new RemoteConnectionFactory
  var sparkContext : SparkSession = _

  def initSparkConnection() : Unit = {
    val configuration = new SparkConf(true).setMaster("local[8]")
                        .setAppName("price-probe")
                        .set("spark.cassandra.connection.host", remoteFactory.host)
                        .set("spark.cassandra.connection.port", remoteFactory.forwardPort.toString)
                        .set("spark.cassandra.input.consistency.level","ONE")
                        .set("spark.driver.allowMultipleContexts", "true")
    val sparkSession = SparkSession
      .builder()
      .appName("SparkSessionZipsExample")
      .config(configuration)
      .enableHiveSupport()
      .getOrCreate()

    sparkContext = sparkSession
  }

  def getSparkInstance : SparkSession = {
    sparkContext
  }

}
