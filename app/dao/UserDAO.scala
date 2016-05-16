package dao

import javax.inject.{Inject, Singleton}

import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import util.MyPostgresDriver

import scala.concurrent.Future

@Singleton()
class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends AuthTokensComponent with HasDatabaseConfigProvider[MyPostgresDriver] {
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

  def getByUsername(username: String): Future[Option[User]] = db.run {
    Users.filter(_.username === username).result.headOption
  }

  def getById(id: Int): Future[Option[User]] = db.run {
    Users.filter(_.id === id).result.headOption
  }

  class UserTable(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Int]("id", O.PrimaryKey)
    def username = column[String]("username")
    def password = column[String]("password")

    def * = (id, username, password) <> ((User.apply _).tupled, User.unapply _)
  }
}