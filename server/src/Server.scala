package server

import java.io.File

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.mongodb.{MongoCredential, ServerAddress}
import item._
import com.mongodb.casbah.Imports._

import _root_.scala.io.StdIn
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor

/**
  * Created by andream16 on 22.06.17.
  */
object MainRouter {
  val routes = ItemEndpoint.route
}

object Server {

  val configFile = new File("./config/application.conf")
  val fileConfig: Config = ConfigFactory.parseFile(configFile)
  val config: Config = ConfigFactory.load(fileConfig)

  val host: String = getConf("server", "api")
  val port: Int = Integer.parseInt(getConf("server", "port"))
  val sshUrl: String = getConf("credentials", "ssh-url")
  val sshPort: String = getConf("credentials", "ssh-port")
  val sshUserName: String = getConf("credentials", "user-name")
  val sshPassword: String = getConf("credentials", "password")

  implicit val system = ActorSystem("simple-rest-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val requestHandler: ActorRef = system.actorOf(RequestHandler.props(), "requestHandler")

  def main(args: Array[String]): Unit = {

    val uri = MongoClientURI("mongodb://" + sshUserName + ":" + sshPassword + "@" + sshUrl + ":" + sshPort + "/")
    val mongoClient =  MongoClient(uri)

    val dbs = mongoClient.getDatabaseNames()
    print(dbs)

    //Startup, and listen for requests
    val bindingFuture = Http().bindAndHandle(MainRouter.routes, host, port)
    println(s"Waiting for requests at http://$host:$port/...\nHit RETURN to terminate")
    StdIn.readLine()

    //Shutdown
    bindingFuture.flatMap(_.unbind())
    system.terminate()

  }

  def getConf(group: String, key: String): String = {
    config.getString("config." + group + "." + key + ".value")
  }

}
