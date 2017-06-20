import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

/**
  * Created by andream16 on 20.06.17.
  */

trait HealthJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val healthFormat = jsonFormat2(Health)
}
