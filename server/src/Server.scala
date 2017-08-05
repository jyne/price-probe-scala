package priceprobe.server

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import priceprobe.item._
import priceprobe.connection.{RemoteConnectionFactory}

import _root_.scala.io.StdIn
import scala.concurrent.ExecutionContextExecutor

/**
  * Created by andream16 on 22.06.17.
  */
object MainRouter {
  val itemEndPoint : ItemEndpoint = new ItemEndpoint
  val routes: Route = itemEndPoint.route
}

object Server {

  implicit val system = ActorSystem("simple-rest-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val requestHandler: ActorRef = system.actorOf(RequestHandler.props(), "requestHandler")

  def main(args: Array[String]): Unit = {

    val remoteConnectionFactory = new RemoteConnectionFactory
    val host = remoteConnectionFactory.host
    val port = remoteConnectionFactory.port

    //remoteConnectionFactory.initRemoteConnection()

    //Startup, and listen for requests
    val bindingFuture = Http().bindAndHandle(MainRouter.routes, host, port)
    println(s"Waiting for requests at http://$host:$port/...")
    StdIn.readLine()

    //Shutdown
    bindingFuture.flatMap(_.unbind())
    system.terminate()

  }

}
