lazy val scyllaCache = Project(id = "scylla-cache", base = file("."))
  .settings(
    libraryDependencies ++= Seq(
      "com.scylladb" % "scylla-driver-core" % "3.7.1-scylla-2"
    )
  )
  .dependsOn(SubProjects.core)
