package item

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by andream16 on 20.06.17.
  */
object RequestHandler {
  def props(): Props = {
    Props(classOf[RequestHandler])
  }
}

case class Item(id: String, url: String, title: String , available: Boolean, pid: String , stars: Int, rank: Double, price: Double,
                price_average: Double, category: BigInt, img: String, date_modif: String, date_price_changes: String, createdAt: String)
case object GetItemRequest
case class SetStatusRequest(item: Item)
case class ItemResponse(item: Item)

class RequestHandler extends Actor with ActorLogging {

  var status: Item = Item( "594650e56cc12a1735c4781f","http://www.amazon.co.uk/gp/product/B0116DLZ4E","Borgonovo Taza Ischia Espresso Saucer 12cm - Set of 6 - Saucers for Italian Espresso Coffee Cups",
  true, "B0116DLZ4E", 5, 0.9, 3.99, 6.99, 3149388031L, "https://images-eu.ssl-images-amazon.com/images/I/318b5gf6qHL._AC_US160_.jpg", "2017-06-19T16:03:34.022Z",
    "2017-06-19T09:11:17.084", "2017-06-18T10:07:33.416Z")

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
