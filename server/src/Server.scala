package priceprobe.server

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.apache.spark.sql.SparkSession
import priceprobe.item._
import priceprobe.connection.{RemoteConnectionFactory, SparkConnectionFactory}
import priceprobe.price.{PriceEndpoint, PriceRequestHandler}
import akka.http.scaladsl.server.Directives._
import _root_.scala.io.StdIn
import scala.concurrent.ExecutionContextExecutor

/**
  * Created by andream16 on 22.06.17.
  */
object MainRouter {
  val routes = ItemEndpoint.route ~ PriceEndpoint.route
}

object Server {

  implicit val system = ActorSystem("simple-rest-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit var itemRequestHandler: ActorRef = _
  implicit var priceRequestHandler: ActorRef = _
  implicit var sc: SparkSession = _

  def main(args: Array[String]): Unit = {

    val remoteConnectionFactory = new RemoteConnectionFactory
    val host = remoteConnectionFactory.host
    val port = remoteConnectionFactory.port

    remoteConnectionFactory.initRemoteConnection()

    val sparkConnectionFactory = new SparkConnectionFactory
    sparkConnectionFactory.initSparkConnection()
    sc = sparkConnectionFactory.getSparkInstance

    itemRequestHandler = system.actorOf(ItemRequestHandler.props(), "itemRequestHandler")
    priceRequestHandler = system.actorOf(PriceRequestHandler.props(), "priceRequestHandler")

    //Startup, and listen for requests
    val bindingFuture = Http().bindAndHandle(MainRouter.routes, host, port)
    println(s"Waiting for requests at http://$host:$port/...")
    StdIn.readLine()

    //Shutdown
    bindingFuture.flatMap(_.unbind())
    system.terminate()

  }

}
