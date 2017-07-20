package priceprobe.item

import org.apache.spark.SparkContext
import priceprobe.connection.SparkConnectionFactory
import com.datastax.spark.connector._

/**
  * Created by andream16 on 20.07.17.
  */
class ItemFactory {

  var page = 1
  var size = 10

  val sparkConnectionFactory = new SparkConnectionFactory
  sparkConnectionFactory.initSparkConnection()
  val sc : SparkContext = sparkConnectionFactory.getSparkInstance

  def getItemById() {
    val rdd = sc.cassandraTable("price_probe", "pricest")
    rdd.foreach(println)
  }

}
