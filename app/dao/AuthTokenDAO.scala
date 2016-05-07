package dao

import javax.inject.Inject

import models.AuthToken
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

class AuthTokenDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val AuthTokens = TableQuery[AuthTokenTable]

  def all(): Future[Seq[AuthToken]] = db.run(AuthTokens.result)

  def insert(authToken: AuthToken): Future[Unit] = db.run(AuthTokens += authToken).map { _ => () }

  private class AuthTokenTable(tag: Tag) extends Table[AuthToken](tag, "AUTH_TOKEN") {

    def token = column[String]("TOKEN", O.PrimaryKey)
    def userId = column[Int]("USER_ID")

    def * = (token, userId) <> (AuthToken.tupled, AuthToken.unapply _)
  }
}