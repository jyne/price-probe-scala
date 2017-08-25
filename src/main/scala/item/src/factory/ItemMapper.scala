package item.src.factory
import com.datastax.driver.core.Row
import org.joda.time.DateTime

import item.src.entity.Item

object ItemMapper {

  def rowToItem()(row: Row): Item = {
    Item(Option(row.getString("item")).getOrElse(""),
      Option(row.getString("category")).getOrElse(""),
      Option(row.getString("description")).getOrElse(""),
      Option(row.getString("img")).getOrElse(""),
      Option(row.getString("pid")).getOrElse(""),
      Option(row.getString("title")).getOrElse(""),
      Option(row.getString("url")).getOrElse(""))
  }

  def dateTimeStringToList(dateTime: String): List[Int] = {
    val d = new DateTime(dateTime)
    List(d.getYear, d.getMonthOfYear, d.getDayOfMonth)
  }

}
