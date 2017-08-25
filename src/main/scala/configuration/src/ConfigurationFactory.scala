package configuration.src

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by andream16 on 25.08.17.
  */
class ConfigurationFactory {

  // Configuration
  val config: Config = ConfigFactory.load("application.conf")

  // Server
  val host: String = getConf("server", "url")
  val port: Int = getConf("server", "port").toInt

  // Cassandra
  val cassandraHost: String = getConf("cassandra", "host")
  val cassandraPort: String = getConf("cassandra", "port")

  /**
    * @param group String
    * @param key String
    * @return String
    * */
  def getConf(group: String, key: String): String = {
    config.getString("config." + group + "." + key + ".value")
  }

}