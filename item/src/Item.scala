package priceprobe.item

import akka.actor.{Actor, ActorLogging, Props}

import priceprobe.server.Server

/**
  * Created by andream16 on 20.06.17.
  */
object ItemRequestHandler {
  def props(): Props = {
    Props(classOf[ItemRequestHandler])
  }
}

case class Item(id: String, category: String, description : String, img : String, pid: String, title: String , url : String)
case class Items(items: List[Item])
case class GetItemsRequest(size: Integer, page: Integer)
case class GetItemByPidRequest(pid: String)
case class GetItemByUrlRequest(url: String)
case class GetItemByTitleRequest(title: String, size: Integer, page: Integer)

class ItemRequestHandler extends Actor with ActorLogging {

  val itemFactory: ItemFactory = new ItemFactory()(Server.sc)

  var item : Item = _
  var items : Items = _

  def receive: Receive = {
    case GetItemsRequest(size: Integer, page: Integer) =>
      items = itemFactory.getItems(size, page)
      sender() !  items
    case GetItemByTitleRequest(title: String, size: Integer, page: Integer) =>
      item = itemFactory.getItemsByTitle(title, size, page)
      sender() !  item
    case GetItemByPidRequest(pid: String) =>
      item = itemFactory.getItemByPid(pid)
      sender() !  item
    case GetItemByUrlRequest(url: String) =>
      item = itemFactory.getItemByUrl(url)
      sender() !  item
  }

}
