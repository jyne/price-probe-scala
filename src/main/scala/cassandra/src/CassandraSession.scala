package cassandra.src

import com.datastax.driver.core.{Cluster, Session}

import configuration.src.ConfigurationFactory

/**
  * Created by andream16 on 25.08.17.
  */
object CassandraSession {

  var cassandraSession: Session = _

  def initCassandraSession(): Unit = {

    val configurationFactory: ConfigurationFactory = new ConfigurationFactory

    implicit val session: Session = new Cluster
    .Builder()
      .addContactPoints(configurationFactory.cassandraHost)
      .withPort(configurationFactory.cassandraPort.toInt)
      .build()
      .connect()

    cassandraSession = session
  }

  def getCassandraSession: Session = {
    cassandraSession
  }

}

