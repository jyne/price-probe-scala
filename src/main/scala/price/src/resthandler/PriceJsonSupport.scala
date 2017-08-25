package price.src.resthandler

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import price.src.entity.{Price, Prices}

/**
  * Created by andream16 on 25.08.17.
  */
trait PriceJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val priceFormat: RootJsonFormat[Price] = jsonFormat4(Price)
  implicit val pricesFormat: RootJsonFormat[Prices] = jsonFormat1(Prices)
}