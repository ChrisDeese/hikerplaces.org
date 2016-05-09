package controllers

import javax.inject.Inject

import dao.{AuthTokenDAO, UserDAO}
import play.api.mvc.{Action, Controller}

class Application @Inject() (userDAO: UserDAO, authTokenDAO: AuthTokenDAO) extends Controller {

  def index = Action { implicit request =>
    Ok("welcome to the website!")
  }

  def login = Action { implicit request =>
    val something = authTokenDAO.all.value.get.getOrElse("")
    Ok(s"welcome to the login page! $something")
  }
}
