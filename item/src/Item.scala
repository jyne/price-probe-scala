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

  var r : Item = _

  var itemFactory : ItemFactory = new ItemFactory

  def receive: Receive = {

    case GetItemRequest =>
      r = itemFactory.getItemById
      sender() !  ItemResponse(r)
    case request: SetStatusRequest =>
      log.debug("Updating Status to {}",request.item)
      r = request.item
      sender() ! ItemResponse(r)
  }

  @Override
  def toString(item : Item): String ={
    item.id + " " + item.title + " " + item.pid + " " + item.category + " " + item.description + " " + item.url + " " + item.img
  }

}
