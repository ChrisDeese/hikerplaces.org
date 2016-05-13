package models

import play.api.libs.json.{Json, Writes}

case class User(id: Int, username: String, password: String)

object User {
  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "username" -> user.username
    )
  }
}

case class AuthToken(token: String, userId: Int)
