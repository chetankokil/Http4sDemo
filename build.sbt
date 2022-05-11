name := "http4sDoobie"

scalaVersion := "2.13.8"

version := "1.0"

libraryDependencies ++= {
  val http4sVersion = "1.0-234-d1a2b53"
  val doobieVersion = "0.8.2"
  val circeVersion = "0.14.1"

  Seq(
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-h2" % doobieVersion,
    "org.tpolecat" %% "doobie-hikari" % doobieVersion,
    "org.tpolecat" %% "doobie-specs2" % doobieVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-config" % "0.8.0",
    "mysql" % "mysql-connector-java" % "5.1.34",
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "ch.qos.logback" % "logback-classic" % "1.0.9"
  )
}

// scalacOptions += "-Ypartial-unification"

resolvers ++= Seq(
  "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
)
