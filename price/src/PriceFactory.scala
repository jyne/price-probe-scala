package priceprobe.price

import java.sql.Timestamp
import java.time.{Instant, ZoneOffset}

import collection.JavaConverters._
import scala.collection.JavaConversions._
import org.apache.spark.sql.{Dataset, Row, SparkSession}

class PriceFactory ()(implicit  sc : SparkSession) extends Serializable {

  import sc.implicits._

  var dt: Dataset[Price] = _

  val createDDL =
    """CREATE TEMPORARY VIEW price_probe_prices
     USING org.apache.spark.sql.cassandra
     OPTIONS (
     table "pricest",
     keyspace "price_probe",
     pushdown "true")"""
  if (sc.catalog.tableExists("price_probe_prices")) {
    sc.catalog.dropTempView("price_probe_prices")
    sc.sql(createDDL).queryExecution.withCachedData
  } else {
    sc.sql(createDDL).queryExecution.withCachedData
  }

  def tuple6ToList[T](t: (T,T,T,T,T,T)): List[T] = List(t._1, t._2, t._3, t._4, t._5, t._6)

  def dateTimeStringToList(dateTime: Instant): Seq[Int] = {
    val d = dateTime.atOffset(ZoneOffset.of("+0"))
    tuple6ToList(d.getYear, d.getMonthValue, d.getDayOfMonth, d.getHour, d.getMinute, d.getSecond).asJava.toList
  }

  def rowToPrice()(row: Row): Price = {
    Price(Option(row.getAs[String]("item")).getOrElse(""),
          Option(row.getAs[Double]("price")).getOrElse(0.0),
          Option(row.getAs[Double]("estimated")).getOrElse(0.0),
          dateTimeStringToList(row.getAs[Timestamp]("date").toInstant)
    )
  }

  def getPricesById(item: String) : Prices = {
    val query = sc.sqlContext.sql("SELECT * FROM price_probe_prices WHERE item=\"" + item + "\" AND estimated > 0")
    try {
      dt = query.map(row => rowToPrice()(row))
      try {
        Prices(dt.collect().toList)
      } catch {
        case e: Exception => getPricesById(item)
      }
    } catch {
      case e: Exception => getPricesById(item)
    }
  }

}