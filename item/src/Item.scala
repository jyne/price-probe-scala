package priceprobe.item

import akka.actor.{Actor, ActorLogging, Props}
import org.apache.spark.sql.SparkSession
import priceprobe.connection.SparkConnectionFactory
import priceprobe.price
import priceprobe.price.Prices

/**
  * Created by andream16 on 20.06.17.
  */
object RequestHandler {
  def props(): Props = {
    Props(classOf[RequestHandler])
  }
}

case class Item(id: String, category: String, description : String, img : String, pid: String, title: String , url : String)
case class Items(items: List[Item])
case class GetItemsRequest(size: Integer, page: Integer)
case class GetItemByPidRequest(pid: String)
case class GetItemByUrlRequest(url: String)
case class GetItemByTitleRequest(title: String, size: Integer, page: Integer)

class RequestHandler extends Actor with ActorLogging {

  var r : Item = _
  var items : Items = _
  var prices : Prices = _
  val sparkConnectionFactory = new SparkConnectionFactory
  val connection : Unit = sparkConnectionFactory.initSparkConnection()
  val sc : SparkSession = sparkConnectionFactory.getSparkInstance
  var itemFactory : ItemFactory = new ItemFactory()(sc)

  def receive: Receive = {
    case GetItemsRequest(size: Integer, page: Integer) =>
      items = itemFactory.getItems(size, page)
      sender() !  items
    case GetItemByTitleRequest(title: String, size: Integer, page: Integer) =>
      r = itemFactory.getItemsByTitle(title, size, page)
      sender() !  r
    case GetItemByPidRequest(pid: String) =>
      prices = itemFactory.getItemByPid(pid)
      sender() !  r
    case GetItemByUrlRequest(url: String) =>
      r = itemFactory.getItemByUrl(url)
      sender() !  r
  }

  @Override
  def toString(item : Item): String ={
    item.id + " " + item.title + " " + item.pid + " " + item.category + " " + item.description + " " + item.url + " " + item.img
  }

}
