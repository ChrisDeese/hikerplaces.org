package models

import com.vividsolutions.jts.geom.{Coordinate, GeometryFactory, Point, PrecisionModel}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class User(id: Int, username: String, password: String)

object User {
  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "username" -> user.username
    )
  }

  implicit val userReads: Reads[User] = (
    ((JsPath \ "id").read[Int] or Reads.pure(0)) and
    (JsPath \ "username").read[String] and
    (JsPath \ "password").read[String]
    )(User.apply _)
}

// todo: replace me with oauth
case class AuthToken(token: String, userId: Int)

object AuthToken {
  def generate(userId: Int): AuthToken = {
    AuthToken(java.util.UUID.randomUUID.toString, userId)
  }

  implicit val authTokenWrites = new Writes[AuthToken] {
    def writes(authToken: AuthToken) = Json.obj(
      "token" -> authToken.token,
      "userId" -> authToken.userId
    )
  }
}

case class Place(id: Int, name: String, geom: Point)

object Place {
  private final val SRID = 4326

  // todo: serialize/deserialize as geojson
  implicit val placeWrites = new Writes[Place] {
    def writes(place: Place) = Json.obj(
      "id" -> place.id,
      "name" -> place.name,
      "geom" -> Json.obj(
        "lat" -> place.geom.getX,
        "lon" -> place.geom.getY
      )
    )
  }

  implicit val placeReads: Reads[Place] = (
      ((JsPath \ "id").read[Int] or Reads.pure(0)) and
      (JsPath \ "name").read[String] and
      (JsPath \ "geom" \ "lat").read[Double] and
      (JsPath \ "geom" \ "lon").read[Double]
    )((id, name, lat, lon) => {
      val geomFac = new GeometryFactory(new PrecisionModel(), SRID)
      val point = geomFac.createPoint(new Coordinate(lat, lon))
      Place(id, name, point)
    })
}

case class Article(id: Int, name: String, revisionId: Int)

case class ArticleRevision(id: Int, text: String)
