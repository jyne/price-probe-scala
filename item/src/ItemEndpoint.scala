package priceprobe.item

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.duration._
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import priceprobe.server.Server

/**
  * Created by andream16 on 20.06.17.
  */
object ItemEndpoint extends ItemJsonSupport {

    var res : Item = _

    val route: Route = cors() {

      implicit val timeout = Timeout(20.seconds)

      path("item") {
        get {
          parameters("key".?, "value".?, "size".?, "page".?) {
            (key, value, size, page) =>
              (key, value, size, page) match {
                case (Some(k), Some(v), Some(s), Some(p)) => (k, v, s, p) match {
                  case ("title", _, _, _) => onSuccess(Server.itemRequestHandler ? GetItemByTitleRequest(v, s.toInt, k.toInt)) {
                    case response: Item =>
                      complete(response)
                    case _ =>
                      complete(StatusCodes.InternalServerError)
                  }
                }
                case (Some(k), Some(v), _, _) => (k, v) match {
                  case ("item", _) => onSuccess(Server.itemRequestHandler ? GetItemByIdRequest(v)) {
                    case response: Item =>
                      complete(response)
                    case _ =>
                      complete(StatusCodes.InternalServerError)
                  }
                  case ("pid", _) => onSuccess(Server.itemRequestHandler ? GetItemByPidRequest(v)) {
                    case response: Item =>
                      complete(response)
                    case _ =>
                      complete(StatusCodes.InternalServerError)
                  }
                  case ("url", _) => onSuccess(Server.itemRequestHandler ? GetItemByUrlRequest(v)) {
                    case response: Item =>
                      complete(response)
                    case _ =>
                      complete(StatusCodes.InternalServerError)
                  }
                  case (_, _) => complete(StatusCodes.InternalServerError)
                }
                case (_, _, Some(s), Some(p)) => (s, p) match {
                  case (_, _) => onSuccess(Server.itemRequestHandler ? GetItemsRequest(s.toInt, p.toInt)) {
                    case response: Items =>
                      complete(response)
                    case _ =>
                      complete(StatusCodes.InternalServerError)
                  }
                }
                case (_, _, _, _) => complete(StatusCodes.BadRequest)
              }
          }
        }

      }
    }

}
