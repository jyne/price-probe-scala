package priceprobe.item

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.duration._
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import priceprobe.server.Server
import priceprobe.price.Prices


/**
  * Created by andream16 on 20.06.17.
  */

case class itemEndPointException(err: String)  extends Exception(err)

class ItemEndpoint extends ItemJsonSupport {

    var res : Item = _

    val route: Route = cors() {

      implicit val timeout = Timeout(20.seconds)

      path("item") {
        get {
          parameters("key".?, "value".?, "size".?, "page".?) {
            (key, value, size, page) =>
              (key, value, size, page) match {
                case (Some(k), Some(v), _, _) => (k, v) match {
                  case ("pid", _) => onSuccess(Server.requestHandler ? GetItemByPidRequest(v)) {
                    case response: Prices =>
                      complete(response)
                    case _ =>
                      complete(StatusCodes.InternalServerError)
                  }
                  case ("url", _) => onSuccess(Server.requestHandler ? GetItemByUrlRequest(v)) {
                    case response: Item =>
                      complete(response)
                    case _ =>
                      complete(StatusCodes.InternalServerError)
                  }
                  case (_, _) => complete(StatusCodes.InternalServerError)
                }
                case (Some(k), Some(v), Some(s), Some(p)) => (k, v, s, p) match {
                  case ("title", _, _, _) => onSuccess(Server.requestHandler ? GetItemByTitleRequest(v, s.toInt, k.toInt)) {
                    case response: Item =>
                      complete(response)
                    case _ =>
                      complete(StatusCodes.InternalServerError)
                  }
                }
                case (_, _, Some(s), Some(p)) => (s, p) match {
                  case (_, _) => onSuccess(Server.requestHandler ? GetItemsRequest(s.toInt, p.toInt)) {
                    case response: Items =>
                      complete(response)
                    case _ =>
                      complete(StatusCodes.InternalServerError)
                  }
                  case (_, _) => complete(StatusCodes.BadRequest)
                }
              }
          }
        }

      }
    }

}
