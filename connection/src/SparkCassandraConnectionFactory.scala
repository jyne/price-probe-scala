package connection

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by andream16 on 18.07.17.
  */
class SparkCassandraConnectionFactory {

  val remoteFactory = new RemoteConnectionFactory
  var sparkContext : SparkContext = _

  def initSparkCassandraConnection = {
    val configuration = new SparkConf(true).setMaster("local[8]")
                        .setAppName("price-probe")
                        .set("spark.cassandra.connection.host", remoteFactory.host)
                        .set("spark.cassandra.connection.port", remoteFactory.forwardPort.toString)
                        .set("spark.cassandra.input.consistency.level","ONE")
                        .set("spark.driver.allowMultipleContexts", "true")
    val sc = new SparkContext(configuration)
    sparkContext = sc
  }

  def getSparkCassandraInstance : SparkContext = {
    sparkContext
  }

}
