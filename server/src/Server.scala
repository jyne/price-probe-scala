package server

import java.io.{BufferedReader, File, InputStreamReader}
import java.util.Properties

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.jcraft.jsch.{ChannelExec, JSch, Session}
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
  val port: Int = getConf("server", "port").toInt
  val sshUrl: String = getConf("credentials", "ssh-url")
  val sshPort: Int = getConf("credentials", "ssh-port").toInt
  val mongoPort: Int = getConf("credentials", "mongo-port").toInt
  val forwardPort: Int = getConf("credentials", "forward-port").toInt
  val sshUserName: String = getConf("credentials", "user-name")
  val sshPassword: String = getConf("credentials", "password")
  val sshTimeout : Int = getConf("credentials", "timeout").toInt

  implicit val system = ActorSystem("simple-rest-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val requestHandler: ActorRef = system.actorOf(RequestHandler.props(), "requestHandler")

  def main(args: Array[String]): Unit = {

    val jsch: JSch = new JSch()
    val session: Session = jsch.getSession(sshUserName, sshUrl, sshPort)
    session.setPassword(sshPassword)

    val config = new Properties()
    config.put("StrictHostKeyChecking", "no")
    session.setConfig(config)
    session.connect()

    val channel = session.openChannel("exec").asInstanceOf[ChannelExec]
    val in = new BufferedReader(new InputStreamReader(channel.getInputStream))
    channel.connect()
    session.setPortForwardingL(forwardPort, host, mongoPort)

    val uri = new MongoClientURI("mongodb://localhost:8988/")
    val mongoClient = MongoClient(uri)
    val dbs = mongoClient.getDatabaseNames()
    print(dbs)

    channel.disconnect()
    session.disconnect()

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
