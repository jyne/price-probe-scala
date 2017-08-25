package price.src.factory

import java.time.{Instant, ZoneOffset}
import com.datastax.driver.core.Row
import collection.JavaConverters._
import scala.collection.JavaConversions._

import price.src.entity.Price

/**
  * Created by andream16 on 25.08.17.
  */
object PriceMapper {

  def tuple6ToList[T](t: (T,T,T,T,T,T)): List[T] = List(t._1, t._2, t._3, t._4, t._5, t._6)

  def dateTimeStringToList(dateTime: Instant): Seq[Int] = {
    val d = dateTime.atOffset(ZoneOffset.of("+0"))
    tuple6ToList(d.getYear, d.getMonthValue, d.getDayOfMonth, d.getHour, d.getMinute, d.getSecond).asJava.toList
  }

  def rowToPrice()(row: Row): Price = {
    Price(Option(row.getString("item")).getOrElse(""),
      Option(row.getDouble("price")).getOrElse(0.0),
      Option(row.getDouble("estimated")).getOrElse(0.0),
      dateTimeStringToList(row.getTimestamp("date").toInstant)
    )
  }

}
