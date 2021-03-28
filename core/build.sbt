val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.4"
val circeVersion = "0.12.3"

lazy val core = (project in file(".")).settings(
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.chuusai" %% "shapeless" % "2.4.0-M1",
    "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",
    "org.scala-lang" % "scala-reflect" % "2.12.10"
  ) ++ Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)
)
