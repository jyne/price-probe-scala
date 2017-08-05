package priceprobe.price

import org.joda.time.DateTime

/**
  * Created by andream16 on 20.06.17.
  */
case class Price(item: String, price: Double, estimated: Double, date: DateTime)
case class Prices(prices : List[Price])