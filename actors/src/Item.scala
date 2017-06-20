/**
  * Created by andream16 on 20.06.17.
  */

import akka.actor.{Actor, ActorLogging, Props}

object RequestHandler {
  def props(): Props = {
    Props(classOf[RequestHandler])
  }
}

case class Item(status: String, description: String)
case object GetItemRequest
case class SetStatusRequest(item: Item)
case class ItemResponse(item: Item)

class RequestHandler extends Actor with ActorLogging{

  var status: Item = Item("Healthy","Initialized")

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
