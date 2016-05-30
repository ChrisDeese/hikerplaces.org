package dao

import javax.inject.{Inject, Singleton}

import models.Article
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import util.MyPostgresDriver
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

@Singleton()
class ArticleDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver] {
  import driver.api._

  private val Articles = TableQuery[ArticleTable]

  def all(): Future[Seq[Article]] = db.run(Articles.result)

  class ArticleTable(tag: Tag) extends Table[Article](tag, "articles") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def revisionId = column[Int]("revision_id")
    def * = (id, name, revisionId) <> ((Article.apply _).tupled, Article.unapply _)
  }
}
