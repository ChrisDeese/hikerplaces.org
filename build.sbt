name := """hikerplaces"""

resolvers += Resolver.sonatypeRepo("releases")

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  "com.h2database" % "h2" % "1.4.191"
)

lazy val root = project.in(file(".")).enablePlugins(PlayScala)
