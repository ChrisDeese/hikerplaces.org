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

  class UserTable(tag: Tag) extends Table[User](tag, "USER") {

    def id = column[Int]("ID", O.PrimaryKey)
    def name = column[String]("USERNAME")
    def password = column[String]("PASSWORD")

    def * = (id, name, password) <> (User.tupled, User.unapply _)
  }
}