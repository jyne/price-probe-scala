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

  def getItems : Item = {
    val rdd = sc.cassandraTable("price_probe", "itemst")
    val r = rdd.map(row => Item(row.getString("item"), row.getString("pid"), row.getString("img"), row.getString("description"),
      row.getString("title"), row.getString("category"), row.getString("url"))
    )
    val item = r.filter( i => i.id == "561667d23574e5fb1a8063a3")
    item.first()
  }

  def getItemByPid : Item = {
    val rdd = sc.cassandraTable("price_probe", "itemst")
    val r = rdd.map(row => Item(row.getString("item"), row.getString("pid"), row.getString("img"), row.getString("description"),
    row.getString("title"), row.getString("category"), row.getString("url"))
    )
    val item = r.filter( i => i.id == "561667d23574e5fb1a8063a3")
    item.first()
  }

  def getItemByUrl : Item = {
    val rdd = sc.cassandraTable("price_probe", "itemst")
    val r = rdd.map(row => Item(row.getString("item"), row.getString("pid"), row.getString("img"), row.getString("description"),
    row.getString("title"), row.getString("category"), row.getString("url"))
    )
    val item = r.filter( i => i.id == "561667d23574e5fb1a8063a3")
    item.first()
  }

  def getItemsByTitle : Item = {
    val rdd = sc.cassandraTable("price_probe", "itemst")
    val r = rdd.map(row => Item(row.getString("item"), row.getString("pid"), row.getString("img"), row.getString("description"),
      row.getString("title"), row.getString("category"), row.getString("url"))
    )
    val item = r.filter( i => i.id == "561667d23574e5fb1a8063a3")
    item.first()
  }

}
