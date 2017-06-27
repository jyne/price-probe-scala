package connection

import com.mongodb.casbah.Imports.{MongoClient, MongoClientURI}

/**
  * Created by andream16 on 27.06.17.
  */
class MongoClientFactory() {

  var mongoClient : MongoClient = _

  def initMongoClient(): Unit = {
    val uri = new MongoClientURI("mongodb://localhost:8988/")
    mongoClient = MongoClient(uri)
  }

  def getMongoClientInstance : MongoClient = {
    mongoClient
  }

  def disconnectMongoClient() : Unit = {
    try {
      mongoClient.close()
    } catch {
      case e : Exception => e.printStackTrace(); None
      case _ : Throwable => None
    } finally {
      None
    }
  }

}
