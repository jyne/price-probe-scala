/**
  * Created by andream16 on 20.06.17.
  */

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask

object endpoints extends HealthJsonSupport {

  import scala.io.StdIn

  val host = "localhost"
  val port = 8080

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("simple-rest-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher


    val requestHandler = system.actorOf(RequestHandler.props(), "requestHandler")

    //Define the route
    val route: Route = {

      implicit val timeout = Timeout(20.seconds)

      path("health") {
        get {
          onSuccess(requestHandler ? GetHealthRequest) {
            case response: HealthResponse =>
              complete(response.health) // Complete the route and respond with the Health data
            case _ =>
              complete(StatusCodes.InternalServerError)
          }
        } ~
          post {
            entity(as[Health]) { statusReport =>
              onSuccess(requestHandler ? SetStatusRequest(statusReport)) {
                case response: HealthResponse =>
                  complete(response.health) // Complete the route and respond with the Health data
                case _ =>
                  complete(StatusCodes.InternalServerError)
              }
            }
          }
      }

    }

    //Startup, and listen for requests
    val bindingFuture = Http().bindAndHandle(route, host, port)
    println(s"Waiting for requests at http://$host:$port/...\nHit RETURN to terminate")
    StdIn.readLine()

    //Shutdown
    bindingFuture.flatMap(_.unbind())
    system.terminate()
  }

}
