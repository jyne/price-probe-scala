import org.apache.log4j.PropertyConfigurator

import server.src.Server
import cassandra.src.CassandraSession

/**
  * Created by andream16 on 25.08.17.
  */
object Main extends App {

  override def main(args: Array[String]): Unit = {

    println("Setting up Cassandra Session . . . ")
    CassandraSession.initCassandraSession()
    println("Setting up routes and serving the app . . .")
    val log4jConfPath = "src/main/resources/log4j.properties"
    PropertyConfigurator.configure(log4jConfPath)
    Server.connect()

  }

}
