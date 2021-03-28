lazy val examples = (project in file("."))
  .dependsOn(SubProjects.core)
  .dependsOn(SubProjects.api)
  .dependsOn(SubProjects.scyllaCache)
