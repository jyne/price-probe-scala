package item.src.resthandler

import akka.actor.{Actor, ActorLogging, Props}

import item.src.entity._
import item.src.factory.ItemFactory

/**
  * Created by andream16 on 25.08.17.
  */
object ItemRequestHandler {
  def props(): Props = {
    Props(classOf[ItemRequestHandler])
  }
}

case class GetItemsRequest(size: Integer, page: Integer)
case class GetItemByIdRequest(id: String)
case class GetItemByPidRequest(pid: String)
case class GetItemByUrlRequest(url: String)
case class GetItemByTitleRequest(title: String, size: Integer, page: Integer)

class ItemRequestHandler extends Actor with ActorLogging {

    var item : Item = _
    var items : Items = _
    val itemFactory: ItemFactory = new ItemFactory()

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
      case GetItemByIdRequest(id: String) =>
        item = itemFactory.getItemById(id)
        sender() !  item
    }

}
