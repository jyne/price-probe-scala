package server

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import item._
import com.mongodb.casbah.Imports._
import connection.{MongoClientFactory, RemoteConnectionFactory}

import _root_.scala.io.StdIn
import scala.concurrent.ExecutionContextExecutor

/**
  * Created by andream16 on 22.06.17.
  */
object MainRouter {
  val routes: Route = ItemEndpoint.route
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

    remoteConnectionFactory.initRemoteConnection()
    val connectionPool = remoteConnectionFactory.getRemoteConnection

    //Connect to local MongoDB
    val mongoClientConnection = new MongoClientFactory()
    mongoClientConnection.initMongoClient()
    val mongoClient = mongoClientConnection.getMongoClientInstance
    mongoClientConnection.disconnectMongoClient()

    remoteConnectionFactory.disconnectFromRemoteConnection()

    //Startup, and listen for requests
    val bindingFuture = Http().bindAndHandle(MainRouter.routes, host, port)
    println(s"Waiting for requests at http://$host:$port/...\nHit RETURN to terminate")
    StdIn.readLine()

    //Shutdown
    bindingFuture.flatMap(_.unbind())
    system.terminate()

  }

}
