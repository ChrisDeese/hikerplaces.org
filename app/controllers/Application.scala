package controllers

import javax.inject.Inject

import dao.{AuthTokenDAO, UserDAO}
import play.api.mvc.{Action, Controller}

import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Application @Inject() (userDAO: UserDAO, authTokenDAO: AuthTokenDAO) extends Controller {

  def index = Action.async { implicit request =>
    val futureInt = userDAO.all()
    futureInt.map(users => Ok("number of users: " + users.length))
  }

  def login = Action { implicit request =>
    Ok("not implemented yet")
  }
}
