import sbt.{Project, file}

object SubProjects {
  lazy val core = Project(id = "core", base = file("core"))
  lazy val api = Project(id = "api", base = file("api"))
  lazy val scyllaCache =
    Project(id = "scylla-cache", base = file("scylla-cache"))

  lazy val examples = Project(id = "examples", base = file("examples"))
}
