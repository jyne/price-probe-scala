package priceprobe.item

import org.apache.spark.sql.{Row, SparkSession}
import org.joda.time.DateTime

/**
  * Created by andream16 on 20.07.17.
  */
class ItemFactory ()(implicit  sc : SparkSession) extends Serializable {

  import sc.implicits._

  val createDDL = """CREATE TEMPORARY VIEW price_probe_items
     USING org.apache.spark.sql.cassandra
     OPTIONS (
     table "itemst",
     keyspace "price_probe",
     pushdown "true")"""
  if(sc.catalog.tableExists("price_probe_items")){
    sc.catalog.dropTempView("price_probe_items")
    sc.sql(createDDL)
  } else {
    sc.sql(createDDL)
  }

  def rowToItem()(row: Row): Item = {
    Item(Option(row.getAs[String]("item")).getOrElse(""),
      Option(row.getAs[String]("category")).getOrElse(""),
      Option(row.getAs[String]("description")).getOrElse(""),
      Option(row.getAs[String]("img")).getOrElse(""),
      Option(row.getAs[String]("pid")).getOrElse(""),
      Option(row.getAs[String]("title")).getOrElse(""),
      Option(row.getAs[String]("url")).getOrElse(""))
  }

  def dateTimeStringToList(dateTime: String): List[Int] = {
    val d = new DateTime(dateTime)
    List(d.getYear, d.getMonthOfYear, d.getDayOfMonth)
  }

  def getItems(size: Integer, page: Integer) : Items = {
    val query = sc.sql("SELECT * FROM price_probe_items")
    val dt = query.map(row => rowToItem()(row))
    Items(dt.collect().toList)
    /*val item = List(Item("1", "tennis", "just a test", "miao", "sborra", "lul", "www.goooooogle.com"))
    val items = Items(item)
    items*/
  }

  def getItemByPid(pid : String) : Item = {
    /*val price1 = Price("1", 2.00, 2.10, dateTimeStringToList("2012-08-16T07:22:05Z"))
    val price2 = Price("1", 3.00, 4.10, dateTimeStringToList("2012-09-16T07:22:05Z"))
    val price3 = Price("1", 5.00, 7.10, dateTimeStringToList("2012-10-16T07:22:05Z"))
    Prices(List(price1, price2, price3))*/
    val query = sc.sql("SELECT * FROM price_probe_items WHERE pid=\"" + pid + "\"")
    query.map(row => rowToItem()(row)).first()
  }

  def getItemByUrl(url: String) : Item = {
    val query = sc.sql("SELECT * FROM price_probe_items WHERE url=\"" + url + "\"")
    query.map(row => rowToItem()(row)).first()
  }

  def getItemById(id: String) : Item = {
    val query = sc.sql("SELECT * FROM price_probe_items WHERE item=\"" + id + "\"")
    query.map(row => rowToItem()(row)).first()
  }

  def getItemsByTitle(title: String, size: Integer, page: Integer) : Item = {
    val query = sc.sql("SELECT * FROM price_probe_items WHERE title=\"" + title + "\"")
    query.map(row => rowToItem()(row)).first()
  }

}
