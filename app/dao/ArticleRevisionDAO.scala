package dao

import javax.inject.{Inject, Singleton}

import models.ArticleRevision
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import util.MyPostgresDriver
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

trait ArticleRevisionsComponent { self: HasDatabaseConfigProvider[MyPostgresDriver] =>
  import driver.api._

  class ArticleRevisionTable(tag: Tag) extends Table[ArticleRevision](tag, "article_revisions") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def text = column[String]("text")
    def * = (id, text) <> ((ArticleRevision.apply _).tupled, ArticleRevision.unapply _)
  }
}

@Singleton()
class ArticleRevisionDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
extends ArticleRevisionsComponent with HasDatabaseConfigProvider[MyPostgresDriver] {
  import driver.api._

  private val ArticleRevisions = TableQuery[ArticleRevisionTable]

  def all(): Future[Seq[ArticleRevision]] = db.run(ArticleRevisions.result)

  def insert(articleRevision: ArticleRevision): Future[Unit] = db.run(ArticleRevisions += articleRevision).map { _ => () }
}

