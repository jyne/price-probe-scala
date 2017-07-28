package priceprobe.item

import org.apache.spark.sql.{Row, SparkSession}

/**
  * Created by andream16 on 20.07.17.
  */
class ItemFactory ()(implicit  sc : SparkSession) extends Serializable{

  var page = 1
  var size = 10

  val createDDL = """CREATE TEMPORARY VIEW price_probe
     USING org.apache.spark.sql.cassandra
     OPTIONS (
     table "itemst",
     keyspace "price_probe",
     pushdown "true")"""
  if(sc.catalog.tableExists("price_probe")){
    sc.catalog.dropTempView("price_probe")
    sc.sql(createDDL)
  }
  else {
    sc.sql(createDDL)
  }
  import sc.implicits._

  def rowToItem()(row: Row): Item = {
    Item(Option(row.getAs[String]("item")).getOrElse(""),
      Option(row.getAs[String]("pid")).getOrElse(""),
      Option(row.getAs[String]("img")).getOrElse(""),
      Option(row.getAs[String]("description")).getOrElse(""),
      Option(row.getAs[String]("title")).getOrElse(""),
      Option(row.getAs[String]("category")).getOrElse(""),
      Option(row.getAs[String]("url")).getOrElse(""))
  }

  def getItems(size: Integer, page: Integer) : Items = {
    val query = sc.sql("SELECT * FROM price_probe")
    val dt=query.map(row => rowToItem()(row))

   //Items(dt.collect().toList).items.foreach(println)
    Items(dt.collect().toList)
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
