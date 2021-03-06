package dao

import javax.inject.{Inject, Singleton}

import models.AuthToken
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import util.MyPostgresDriver

import scala.concurrent.Future

trait AuthTokensComponent { self: HasDatabaseConfigProvider[MyPostgresDriver] =>
  import driver.api._

  class AuthTokenTable(tag: Tag) extends Table[AuthToken](tag, "auth_tokens") {
    def token = column[String]("token", O.PrimaryKey)
    def userId = column[Int]("user_id")
    def * = (token, userId) <> ((AuthToken.apply _).tupled, AuthToken.unapply _)
  }
}

@Singleton()
class AuthTokenDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  // todo: replace me with oauth
  extends AuthTokensComponent with HasDatabaseConfigProvider[MyPostgresDriver] {
  import driver.api._

  private val AuthTokens = TableQuery[AuthTokenTable]

  def all(): Future[Seq[AuthToken]] = db.run(AuthTokens.result)

  def insert(authToken: AuthToken): Future[Unit] = db.run(AuthTokens += authToken).map { _ => () }

}