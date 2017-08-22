package priceprobe.price

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait PriceJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val priceFormat: RootJsonFormat[Price] = jsonFormat4(Price)
  implicit val pricesFormat: RootJsonFormat[Prices] = jsonFormat1(Prices)
}