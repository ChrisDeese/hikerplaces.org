name := """hikerplaces"""

resolvers += Resolver.sonatypeRepo("releases")

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.github.tminglei" %% "slick-pg_jts" % "0.14.0",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.14.0",
  "com.github.tminglei" %% "slick-pg_date2" % "0.14.0",
  "com.github.tminglei" %% "slick-pg" % "0.14.0"
)

lazy val root = project.in(file(".")).enablePlugins(PlayScala)
