package priceprobe.price

import java.sql.Timestamp
import java.time.{LocalDate, ZoneId}
import collection.JavaConverters._
import scala.collection.JavaConversions._
import org.apache.spark.sql.{Row, SparkSession}

class PriceFactory ()(implicit  sc : SparkSession) extends Serializable {

  import sc.implicits._

  val createDDL =
    """CREATE TEMPORARY VIEW price_probe_prices
     USING org.apache.spark.sql.cassandra
     OPTIONS (
     table "pricest",
     keyspace "price_probe",
     pushdown "true")"""
  if (sc.catalog.tableExists("price_probe_prices")) {
    sc.catalog.dropTempView("price_probe_prices")
    sc.sql(createDDL)
  } else {
    sc.sql(createDDL)
  }

  def tuple3ToList[T](t: (T,T, T)): List[T] = List(t._1, t._2, t._3)

  def dateTimeStringToList(dateTime: LocalDate): Seq[Int] = {
    tuple3ToList(dateTime.getYear, dateTime.getMonthValue, dateTime.getDayOfMonth).asJava.toList
  }

  def rowToPrice()(row: Row): Price = {
    Price(Option(row.getAs[String]("item")).getOrElse(""),
          Option(row.getAs[Double]("price")).getOrElse(0.0),
          Option(row.getAs[Double]("estimated")).getOrElse(0.0),
          dateTimeStringToList(row.getAs[Timestamp]("date").toLocalDateTime.atZone(ZoneId.of("GMT+0")).toLocalDate)
    )
  }

  def getPricesById(item: String) : Prices = {
    val query = sc.sql("SELECT * FROM price_probe_prices WHERE item=\"" + item + "\"")
    val dt = query.map(row => rowToPrice()(row))
    Prices(dt.collect().toList)
  }

}