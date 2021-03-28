lazy val scyllaCache = Project(id = "scylla-cache", base = file("."))
  .settings(
    organization := "com.github.pxsdirac",
    libraryDependencies ++= Seq(
      "com.scylladb" % "scylla-driver-core" % "3.7.1-scylla-2"
    )
  )
  .dependsOn(SubProjects.core)
