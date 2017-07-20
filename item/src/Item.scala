package priceprobe.item

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by andream16 on 20.06.17.
  */
object RequestHandler {
  def props(): Props = {
    Props(classOf[RequestHandler])
  }
}

case class Item(id: String, pid: String, title: String, description : String, category: String, url : String, img : String)
case object GetItemRequest
case class SetStatusRequest(item: Item)
case class ItemResponse(item: Item)

class RequestHandler extends Actor with ActorLogging {

  var status: Item = Item( "594650e56cc12a1735c4781f", "B0116DLZ4E", "Borgonovo Taza Ischia Espresso Saucer 12cm - Set of 6 - Saucers for Italian Espresso Coffee Cups", "desc",
  "3149388031L", "http://www.amazon.co.uk/gp/product/B0116DLZ4E", "https://images-eu.ssl-images-amazon.com/images/I/318b5gf6qHL._AC_US160_.jpg")

  def receive: Receive = {

    case GetItemRequest =>
      log.debug("Received GetHealthRequest")
      sender() !  ItemResponse(status)
    case request: SetStatusRequest =>
      log.debug("Updating Status to {}",request.item)
      status = request.item
      sender() ! ItemResponse(status)
  }
}
