package dao

import javax.inject.Inject

import models.AuthToken
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

trait AuthTokensComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import driver.api._

  class AuthTokenTable(tag: Tag) extends Table[AuthToken](tag, "AUTH_TOKEN") {
    def token = column[String]("TOKEN", O.PrimaryKey)
    def userId = column[Int]("USER_ID")
    def * = (token, userId) <> (AuthToken.tupled, AuthToken.unapply _)
  }
}

//@Singleton()
class AuthTokenDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends AuthTokensComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val AuthTokens = TableQuery[AuthTokenTable]

  def all(): Future[Seq[AuthToken]] = db.run(AuthTokens.result)

  def insert(authToken: AuthToken): Future[Unit] = db.run(AuthTokens += authToken).map { _ => () }

}