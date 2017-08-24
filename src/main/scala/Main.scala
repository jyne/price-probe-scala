import priceprobe.connection.RemoteConnectionFactory
import priceprobe.server.Server

object Main extends App {

  implicit var remoteConnectionFactory: RemoteConnectionFactory = _

  override def main(args: Array[String]): Unit = {

    //remoteConnectionFactory = new RemoteConnectionFactory
    //remoteConnectionFactory.initRemoteConnection()
    Server.connect

  }

}
