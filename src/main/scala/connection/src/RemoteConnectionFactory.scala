package priceprobe.connection

import java.io.File
import java.util.Properties
import com.jcraft.jsch.{ChannelExec, JSch, Session}
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by andream16 on 27.06.17.
  */
class RemoteConnectionFactory {

  // Configuration
  val configFile = new File(getClass.getClassLoader.getResource("application.conf").getPath)
  val fileConfig: Config = ConfigFactory.parseFile(configFile)
  val config: Config = ConfigFactory.load(fileConfig)

  // Server
  val host: String = getConf("server", "url")
  val port: Int = getConf("server", "port").toInt
  val remoteHost: String = getConf("server", "remote-host")

  // Ssh
  val sshUrl: String = getConf("ssh-credentials", "url")
  val sshPort: Int = getConf("ssh-credentials", "port").toInt
  val sshUserName: String = getConf("ssh-credentials", "username")
  val sshPassword: String = getConf("ssh-credentials", "password")
  val sshPemPath: String = getConf("ssh-credentials", "pem-path")
  val sshTimeout : Int = getConf("ssh-credentials", "timeout").toInt

  // Spark
  val cassandraSubnet: String = getConf("spark", "cassandra-subnet")
  val cassandraPort: Int = getConf("spark", "cassandra-port").toInt
  val cassandraForwardPort: Int = getConf("spark", "cassandra-forward-port").toInt
  val sparkSubnet: String = getConf("spark", "spark-subnet")
  val sparkPort: Int = getConf("spark", "spark-port").toInt
  val sparkForwardPort: Int = getConf("spark", "spark-forward-port").toInt

  var session : Session = _
  var channel : ChannelExec = _

  def initRemoteConnection(): Unit = {
    //Connect remotely via SSH
    val jsch: JSch = new JSch()
    //jsch.addIdentity(sshPemPath)
    session = jsch.getSession(sshUserName, sshUrl, cassandraPort)
    session.setPassword(sshPassword)
    val config = new Properties()
    config.put("StrictHostKeyChecking", "no")
    session.setTimeout(sshTimeout)
    session.setConfig(config)
    session.connect()

    //Prepare Execution
    channel = session.openChannel("exec").asInstanceOf[ChannelExec]
    channel.connect()

    session.setPortForwardingR(sparkPort, sparkSubnet, sparkPort)
    session.setPortForwardingR(cassandraPort, cassandraSubnet, cassandraPort)

  }

  def getRemoteConnection: (ChannelExec, Session) = {
    (channel, session)
  }

  def disconnectFromRemoteConnection(): Unit = {
    try {
      channel.disconnect()
    } catch {
      case e : Exception => e.printStackTrace(); None
      case _ : Throwable => None
    } finally {
      try {
        session.disconnect()
      } catch {
        case e : Exception => e.printStackTrace(); None
        case _ : Throwable => None
      } finally {
        None
      }
    }
  }

  def getConf(group: String, key: String): String = {
    config.getString("config." + group + "." + key + ".value")
  }

}