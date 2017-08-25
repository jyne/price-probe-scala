package item.src.resthandler

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import item.src.entity.{Item, Items}

/**
  * Created by andream16 on 20.06.17.
  */
trait ItemJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat: RootJsonFormat[Item] = jsonFormat7(Item)
  implicit val itemsFormat: RootJsonFormat[Items] = jsonFormat1(Items)
}