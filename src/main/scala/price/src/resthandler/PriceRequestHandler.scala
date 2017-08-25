package price.src.resthandler

import akka.actor.{Actor, ActorLogging, Props}

import price.src.entity.Prices
import price.src.factory.PriceFactory

/**
  * Created by andream16 on 25.08.17.
  */
object PriceRequestHandler {
  def props(): Props = {
    Props(classOf[PriceRequestHandler])
  }
}

case class GetPricesById(item: String)

class PriceRequestHandler extends Actor with ActorLogging {

    var prices: Prices = _
    val priceFactory: PriceFactory = new PriceFactory()

    def receive: Receive = {
      case GetPricesById(item: String) =>
        prices = priceFactory.getPricesById(item)
        sender() !  prices
    }

}
