package priceprobe.item

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.duration._
import akka.pattern.ask
import priceprobe.server.Server

/**
  * Created by andream16 on 20.06.17.
  */
class ItemEndpoint extends ItemJsonSupport {

    var res : Item = _

    //Define the route
    val route: Route = {

      implicit val timeout = Timeout(20.seconds)

      path("item") {
        get {
          parameters("key".?, "value".?) {
            (key, value) => (key, value) match {
              case (Some(k), Some(v)) => println("Ok")
              case (Some(k), None) => println("No Value found")
              case (None, Some(k)) => println("No Key found")
              case (None, None) => println("Nil")
            }
            onSuccess(Server.requestHandler ? GetItemRequest) {
             case response: ItemResponse =>
               complete(response.item)
             case _ =>
               complete(StatusCodes.InternalServerError)
            }
          }

        }
      }

    }

}
