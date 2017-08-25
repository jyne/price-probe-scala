package server.src

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import _root_.scala.io.StdIn
import scala.concurrent.ExecutionContextExecutor
import configuration.src.ConfigurationFactory

import item.src.resthandler.{ItemEndpoint, ItemRequestHandler}
import price.src.resthandler.{PriceEndpoint, PriceRequestHandler}

/**
  * Created by andream16 on 25.08.17.
  */
object MainRouter {
  val routes: Route = ItemEndpoint.route ~ PriceEndpoint.route
}

object Server {

  // Setting up AKKA
  implicit val system = ActorSystem("simple-rest-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit var itemRequestHandler: ActorRef = _
  implicit var priceRequestHandler: ActorRef = _

  def connect(): Unit = {

    // Getting configuration for Server
    val configurationFactory = new ConfigurationFactory

    val host = configurationFactory.host
    val port = configurationFactory.port

    // Setting up endpoints
    priceRequestHandler = system.actorOf(PriceRequestHandler.props(), "PriceRequestHandler")
    itemRequestHandler  = system.actorOf(ItemRequestHandler.props(), "ItemRequestHandler")

    //Startup, and listen for requests
    val bindingFuture = Http().bindAndHandle(MainRouter.routes, host, port)
    println(s"Waiting for requests at http://$host:$port/...")
    StdIn.readLine()

    //Shutdown
    bindingFuture.flatMap(_.unbind())
    system.terminate()

  }

}
