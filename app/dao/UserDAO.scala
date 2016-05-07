package dao

import scala.concurrent.Future
import javax.inject.Inject

import models.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Users = TableQuery[UserTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def insert(user: User): Future[Unit] = db.run(Users += user).map { _ => () }

  class UserTable(tag: Tag) extends Table[User](tag, "USER") {

    def id = column[Int]("ID", O.PrimaryKey)
    def name = column[String]("USERNAME")
    def password = column[String]("PASSWORD")

    def * = (id, name, password) <> (User.tupled, User.unapply _)
  }
}