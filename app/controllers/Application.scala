package controllers

import javax.inject.Inject

import dao.{AuthTokenDAO, UserDAO}
import models.AuthToken
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, JsPath, Json, Reads}
import play.api.mvc.{Action, BodyParsers, Controller}
import play.api.libs.functional.syntax._

import scala.concurrent.Future

class Application @Inject() (userDAO: UserDAO, authTokenDAO: AuthTokenDAO) extends Controller {

  def index = Action.async { implicit request =>
    val futureInt = userDAO.all()
    futureInt.map(users => Ok("number of users: " + users.length))
  }

  def listUsers = Action.async { implicit request =>
    userDAO.all().map(users => Ok(Json.toJson(users)))
  }

  def getUser(id: Int) = Action.async { implicit request =>
    userDAO.getById(id).map {
      case Some(user) => Ok(Json.toJson(user))
      case None => NotFound("user not found")
    }
  }

  def login = Action.async(BodyParsers.parse.json) { implicit request =>
    case class LoginRequest(username: String, password: String)

    implicit val readLoginRequest: Reads[LoginRequest] = (
        (JsPath \ "username").read[String] and
        (JsPath \ "password").read[String]
    )(LoginRequest.apply _)

    request.body.validate[LoginRequest].fold(
      errors => {
        Future(BadRequest(JsError.toJson(errors)))
      },
      loginRequest => {
        userDAO.getByUsername(loginRequest.username).flatMap {
          case Some(user) if user.password == loginRequest.password =>
            val authToken = AuthToken.generate(user.id)
            authTokenDAO.insert(authToken).map { _ =>
              Ok(Json.toJson(authToken))
            }
          case _ => Future(Unauthorized("invalid username or password"))
        }
      }
    )
  }
}
