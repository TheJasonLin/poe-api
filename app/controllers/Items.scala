package controllers

import com.poe.parser.item.{DBItem, Mod}
import org.mongodb.scala.bson.ObjectId
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsValue, Json, OWrites, Writes}
import play.api.mvc.{Action, AnyContent, Controller}
import services.Database


class Items extends Controller {
  implicit val objectIdWrites = new Writes[ObjectId] {
    def writes(objectId: ObjectId): JsValue = Json.obj(
      "$oid" -> objectId.toString
    )
  }
  implicit val modWrites: OWrites[Mod] = Json.writes[Mod]
  implicit val dBItemWrites: OWrites[DBItem] = Json.writes[DBItem]

  def get(className: String): Action[AnyContent] = Action.async {
    Database
      .get(className)
      .map((dbItems: Seq[DBItem]) => {
        Ok(Json.toJson(dbItems))
      })
  }
}
