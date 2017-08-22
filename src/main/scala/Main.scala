import priceprobe.server.Server
import priceprobe.connection.RemoteConnectionFactory

object Main extends App {

  implicit var remoteConnectionFactory: RemoteConnectionFactory = _

  override def main(args: Array[String]): Unit = {

    remoteConnectionFactory = new RemoteConnectionFactory
    remoteConnectionFactory.initRemoteConnection()
    Server.connect

  }

}
