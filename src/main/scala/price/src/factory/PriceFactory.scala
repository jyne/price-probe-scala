package price.src.factory

import monix.execution.Ack
import monix.execution.Scheduler.Implicits.global
import cassandra.src.CassandraHelper
import com.datastax.driver.core.Row
import monix.reactive.Observable
import price.src.entity.{Price, Prices}

/**
  * Created by andream16 on 25.08.17.
  */
class PriceFactory () extends Serializable {

  val keyspace = "price_probe"
  val table = "prices"

  def getPricesById(item: String) : Prices = {
    var prices: List[Price] = List()
    val observable: Observable[Row] = CassandraHelper.executeQuery(
      "SELECT * FROM " + keyspace + "." + table + " where item in (?)",
      1,
      item
    )
    observable.subscribe { row =>
      prices ::= PriceMapper.rowToPrice()(row)
      Ack.Continue
    }
    Prices(prices)
  }

}