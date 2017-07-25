package priceprobe.item

import priceprobe.connection.SparkConnectionFactory
import org.apache.spark.sql.SparkSession

/**
  * Created by andream16 on 20.07.17.
  */
class ItemFactory {

  var page = 1
  var size = 10

  val sparkConnectionFactory = new SparkConnectionFactory
  sparkConnectionFactory.initSparkConnection()
  val sc : SparkSession = sparkConnectionFactory.getSparkInstance
  val createDDL = """CREATE TEMPORARY VIEW price_probe
     USING org.apache.spark.sql.cassandra
     OPTIONS (
     table "itemst",
     keyspace "price_probe",
     pushdown "true")"""
  sc.sql(createDDL)
  import sc.implicits._

  def getItems(size: Integer, page: Integer) : Item = {
    val query = sc.sql("SELECT * FROM price_probe WHERE url=" + size + ";").collect()
    val item = query.map(row => Item(row.getAs[String]("item"), row.getAs[String]("pid"), row.getAs[String]("img"), row.getAs[String]("description"),
      row.getAs[String]("title"), row.getAs[String]("category"), row.getAs[String]("url")
    ))
    item(0)
  }

  def getItemByPid(pid : String) : Item = {
    val query = sc.sql("SELECT * FROM price_probe WHERE pid=\"" + pid + "\"")
    query.map(row => Item(row.getAs[String]("item"), row.getAs[String]("pid"), row.getAs[String]("img"), row.getAs[String]("description"),
      row.getAs[String]("title"), row.getAs[String]("category"), row.getAs[String]("url")
    )).first()
  }

  def getItemByUrl(url: String) : Item = {
    val query = sc.sql("SELECT * FROM price_probe WHERE url=\"" + url + "\"")
    query.map(row => Item(row.getAs[String]("item"), row.getAs[String]("pid"), row.getAs[String]("img"), row.getAs[String]("description"),
                                     row.getAs[String]("title"), row.getAs[String]("category"), row.getAs[String]("url")
    )).first()
  }

  def getItemsByTitle(title: String, size: Integer, page: Integer) : Item = {
    val query = sc.sql("SELECT * FROM price_probe WHERE url=" + title + ";").collect()
    val item = query.map(row => Item(row.getAs[String]("item"), row.getAs[String]("pid"), row.getAs[String]("img"), row.getAs[String]("description"),
      row.getAs[String]("title"), row.getAs[String]("category"), row.getAs[String]("url")
    ))
    item(0)
  }

}
