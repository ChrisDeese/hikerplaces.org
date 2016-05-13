package dao

import javax.inject.{Inject, Singleton}

import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

@Singleton()
class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends AuthTokensComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Users = TableQuery[UserTable]
  private val AuthTokens = TableQuery[AuthTokenTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def insert(user: User): Future[Unit] = db.run(Users += user).map { _ => () }

  def getByAuthToken(token: String): Future[User] = db.run {
    (for {
    (a, u) <- AuthTokens join Users on (_.userId === _.id)
    } yield u).result.head
  }

  class UserTable(tag: Tag) extends Table[User](tag, "user") {

    def id = column[Int]("id", O.PrimaryKey)
    def name = column[String]("username")
    def password = column[String]("password")

    def * = (id, name, password) <> ((User.apply _).tupled, User.unapply _)
  }
}