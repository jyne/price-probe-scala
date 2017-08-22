package priceprobe.price

import akka.actor.{Actor, ActorLogging, Props}

import priceprobe.server.Server

/**
  * Created by andream16 on 20.06.17.
  */

object PriceRequestHandler {
  def props(): Props = {
    Props(classOf[PriceRequestHandler])
  }
}

case class Price(item: String, price: Double, estimated: Double, date: Seq[Int])
case class Prices(prices : List[Price])
case class GetPricesById(item: String)

class PriceRequestHandler extends Actor with ActorLogging {

  var prices: Prices = _
  val priceFactory: PriceFactory = new PriceFactory()(Server.sc)

  def receive: Receive = {
    case GetPricesById(item: String) =>
      prices = priceFactory.getPricesById(item)
      sender() !  prices
  }

}