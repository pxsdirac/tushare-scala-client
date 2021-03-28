name := "tushare-scala-client"

version := "0.1"

scalaVersion := "2.12.10"

organization := "com.github.pxsdirac"

lazy val core = SubProjects.core
lazy val api = SubProjects.api
lazy val scyllaCache = SubProjects.scyllaCache
lazy val examples = SubProjects.examples
