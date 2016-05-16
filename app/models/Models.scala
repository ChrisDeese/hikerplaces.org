package models

import com.vividsolutions.jts.geom.Point
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(id: Int, username: String, password: String)

object User {
  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "username" -> user.username
    )
  }

  /*
  implicit val userReads = new Reads[User] {
    def reads(json: JsValue): User = {
      User(
        id=(json \ "id").as[Int],
        username=(json \ "username").as[String],
        password=(json \ "password").as[String]
      )
    }
  }
  */
}

case class AuthToken(token: String, userId: Int)

object AuthToken {
  def generate(userId: Int): AuthToken = {
    val uuid = java.util.UUID.randomUUID.toString
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