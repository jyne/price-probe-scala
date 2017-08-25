package item.src.factory

import monix.execution.Ack
import monix.execution.Scheduler.Implicits.global
import cassandra.src.CassandraHelper
import com.datastax.driver.core.Row
import monix.reactive.{Observable, Observer}
import item.src.entity.{Item, Items}

/**
  * Created by andream16 on 25.08.17.
  */
class ItemFactory () extends Serializable {

  val keyspace = "price_probe"
  val table = "itemst"

  def getItems(size: Integer, page: Integer) : Items = {
    var items: List[Item] = List()

    val observable: Observable[Row] = CassandraHelper.executeQuery(
      "SELECT * FROM " + keyspace + "." + table,
      page
    )
    observable.subscribe { row =>
      items ::= ItemMapper.rowToItem()(row)
      Ack.Continue
    }
    Items(items)
  }

  def getItemByPid(pid : String) : Item = {
    var item: Item = Item("", "", "", "", "", "", "")
    val observable: Observable[Row] = CassandraHelper.executeQuery(
      "SELECT * FROM " + keyspace + "." + table + " where pid = ?",
      1,
      pid
    )
    observable.subscribe { row =>
      item = ItemMapper.rowToItem()(row)
      Ack.Continue
    }
    item
  }

  def getItemByUrl(url: String) : Item = {
    var item: Item = Item("", "", "", "", "", "", "")
    val observable: Observable[Row] = CassandraHelper.executeQuery(
      "SELECT * FROM " + keyspace + "." + table + " where url = ?",
      1,
      url
    )
    observable.subscribe { row =>
      item = ItemMapper.rowToItem()(row)
      Ack.Continue
    }
    item
  }

  def getItemById(id: String) : Item = {
    var item: Item = Item("", "", "", "", "", "", "")
    val observable: Observable[Row] = CassandraHelper.executeQuery(
      "SELECT * FROM " + keyspace + "." + table + " where id = ?",
      1,
      id
    )
    observable.subscribe { row =>
      item = ItemMapper.rowToItem()(row)
      Ack.Continue
    }
    item
  }

  def getItemsByTitle(title: String, size: Integer, page: Integer) : Item = {
    var item: Item = Item("", "", "", "", "", "", "")
    val observable: Observable[Row] = CassandraHelper.executeQuery(
      "SELECT * FROM " + keyspace + "." + table + " where title = ?",
      1,
      title
    )
    observable.subscribe { row =>
      item = ItemMapper.rowToItem()(row)
      Ack.Continue
    }
    item
  }

}
