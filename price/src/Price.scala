package priceprobe.price

/**
  * Created by andream16 on 20.06.17.
  */
case class Price(item: String, price: Double, estimated: Double, date: List[Int])
case class Prices(prices : List[Price])