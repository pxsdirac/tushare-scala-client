lazy val api = (project in file("."))
  .settings(organization := "com.github.pxsdirac")
  .dependsOn(SubProjects.core)
