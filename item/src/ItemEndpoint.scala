package item

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import server.Server

/**
  * Created by andream16 on 20.06.17.
  */
object ItemEndpoint extends ItemJsonSupport {

    //Define the route
    val route: Route = {

      implicit val timeout = Timeout(20.seconds)

      path("item") {
        get {
          onSuccess(Server.requestHandler ? GetItemRequest) {
            case response: ItemResponse =>
              complete(response.item)
            case _ =>
              complete(StatusCodes.InternalServerError)
          }
        } ~
          post {
            entity(as[Item]) { statusReport =>
              onSuccess(Server.requestHandler ? SetStatusRequest(statusReport)) {
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
