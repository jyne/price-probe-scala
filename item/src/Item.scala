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
case object GetItemsRequest
case object GetItemByPidRequest
case object GetItemByUrlRequest
case object GetItemByTitleRequest
case class ItemResponse(item: Item)

class RequestHandler extends Actor with ActorLogging {

  var r : Item = _

  var itemFactory : ItemFactory = new ItemFactory

  def receive: Receive = {
    case GetItemsRequest =>
      r = itemFactory.getItems
      sender() !  ItemResponse(r)
    case GetItemByTitleRequest =>
      r = itemFactory.getItemsByTitle
      sender() !  ItemResponse(r)case GetItemByPidRequest =>
      r = itemFactory.getItemByPid
      sender() !  ItemResponse(r)
    case GetItemByUrlRequest =>
      r = itemFactory.getItemByUrl
      sender() !  ItemResponse(r)
  }

  @Override
  def toString(item : Item): String ={
    item.id + " " + item.title + " " + item.pid + " " + item.category + " " + item.description + " " + item.url + " " + item.img
  }

}
