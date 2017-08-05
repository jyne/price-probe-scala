package priceprobe.item

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import priceprobe.price.{Price, Prices}
import spray.json._

/**
  * Created by andream16 on 20.06.17.
  */
trait ItemJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat: RootJsonFormat[Item] = jsonFormat7(Item)
  implicit val itemsFormat: RootJsonFormat[Items] = jsonFormat1(Items)
  implicit val priceFormat: RootJsonFormat[Price] = jsonFormat4(Price)
  implicit val pricesFormat: RootJsonFormat[Prices] = jsonFormat1(Prices)
}