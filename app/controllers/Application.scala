package controllers

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.inject.Inject

import dao.{AuthTokenDAO, PlaceDAO, UserDAO}
import models.{AuthToken, Place, User}
import org.postgresql.util.PSQLException
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, JsPath, Json, Reads}
import play.api.mvc._
import play.api.libs.functional.syntax._

import scala.concurrent.Future
import scala.util.{Failure, Success}

class Application @Inject() (userDAO: UserDAO, authTokenDAO: AuthTokenDAO, placeDAO: PlaceDAO) extends Controller {

  class UserRequest[A](val user: Option[User], request: Request[A]) extends WrappedRequest[A](request)

  object UserAction extends ActionBuilder[UserRequest] with ActionTransformer[Request, UserRequest] {
    def transform[A](request: Request[A]) = {
      // todo: replace me with oauth
      request.headers.get("Authorization").flatMap { authorization =>
        authorization.split(" ").drop(1).headOption.flatMap { encoded =>
          val decoded = new String(Base64.getDecoder.decode(encoded.getBytes(StandardCharsets.UTF_8)))
          decoded.split(":").toList match {
            case username :: token :: Nil => Some {
              userDAO.getByAuthToken(token).map {
                case Some(user) if user.username == username => new UserRequest(Option(user), request)
                case _ => new UserRequest(None, request)
              }
            }
            case _ => None
          }
        }
      } match {
        case Some(userRequest) => userRequest
        case None => Future.successful(new UserRequest(None, request))
      }
    }
  }

  def index = UserAction.async { implicit request =>
    //val futureInt = userDAO.all()
    Future.successful(Ok(Json.toJson(request.user)))
    //Future.successful(Ok(request.headers.toString))
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

  def getPlace(id: Int) = Action.async { implicit request =>
    placeDAO.getById(id).map {
      case Some(place) => Ok(Json.toJson(place))
      case None => NotFound("place not found")
    }
  }

  def addPlace() = Action.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[Place].fold(
      errors => {
        Future(BadRequest(JsError.toJson(errors)))
      },
      place => {
        placeDAO.insert(place).map { id =>
          Ok(Json.toJson(place.copy(id=id)))
        }
      }
    )
  }

  def signup() = Action.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[User].fold(
      errors => {
        Future(BadRequest(JsError.toJson(errors)))
      },
      user => {
        userDAO.getByUsername(user.username).flatMap {
          case Some(_) => Future(Conflict("username already exists"))
          case None =>
            userDAO.insert(user).map { id =>
              Created(Json.toJson(user.copy(id=id)))
            }
        }
      }
    )
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
