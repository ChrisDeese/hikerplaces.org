package dao

import javax.inject.{Inject, Singleton}

import com.vividsolutions.jts.geom.Point
import models.Place
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import util.MyPostgresDriver

import scala.concurrent.Future

@Singleton()
class PlaceDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver] {
  import driver.api._

  private val Places = TableQuery[PlaceTable]

  def all(): Future[Seq[Place]] = db.run(Places.result)

  def insert(place: Place): Future[Int] = db.run {
    (Places returning Places.map(_.id)) += place
  }

  def getById(id: Int): Future[Option[Place]] = db.run {
    Places.filter(_.id === id).result.headOption
  }

  class PlaceTable(tag: Tag) extends Table[Place](tag, "places") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def geom = column[Point]("geom")

    def * = (id, name, geom) <> ((Place.apply _).tupled, Place.unapply _)
  }
}