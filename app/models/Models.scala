package models

case class User(id: Int, username: String, password: String)

case class AuthToken(token: String, userId: Int)