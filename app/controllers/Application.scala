package controllers

import javax.inject.Inject

import dao.UserDAO
import play.api.mvc.{Action, Controller}

class Application @Inject() (userDAO: UserDAO) extends Controller {

  def index = Action { implicit request =>
    Ok("welcome to the website!")
  }
}
