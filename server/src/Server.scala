package server

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.datastax.spark.connector._
import item._
import connection.{RemoteConnectionFactory, SparkCassandraConnectionFactory}
import org.apache.spark.SparkContext

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

    remoteConnectionFactory.initRemoteConnection
    val connectionPool = remoteConnectionFactory.getRemoteConnection

    val sparkCassandraConnectionFactory = new SparkCassandraConnectionFactory
    sparkCassandraConnectionFactory.initSparkCassandraConnection
    val sc : SparkContext = sparkCassandraConnectionFactory.getSparkCassandraInstance

    val rdd = sc.cassandraTable("price_probe", "pricest")
    rdd.foreach(println)

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
