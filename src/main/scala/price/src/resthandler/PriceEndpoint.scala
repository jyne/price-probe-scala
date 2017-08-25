package price.src.resthandler

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import scala.concurrent.duration._
import akka.pattern.ask

import price.src.entity.Prices
import server.src.Server

/**
  * Created by andream16 on 25.08.17.
  */
object PriceEndpoint extends PriceJsonSupport {

  var prices: Prices = _

  val route: Route = cors() {

    implicit val timeout = Timeout(60.seconds)

    path("price") {
      get {
        parameters("key".?, "value".?) {
          (key, value) => (key, value) match {
            case (Some(k), Some(v)) => (k, v) match {
                case ("item", _) => onSuccess(Server.priceRequestHandler ? GetPricesById(v)) {
                  case response: Prices =>
                    complete(response)
                  case _ =>
                    complete(StatusCodes.InternalServerError)
                }
                case(_, _) => complete(StatusCodes.InternalServerError)
            }
            case (_, _) => complete(StatusCodes.BadRequest)
          }
        }
      }
    }

  }

}