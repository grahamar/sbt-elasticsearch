
name := "sbt-elasticsearch"

organization := "io.grhodes.sbt"

version := "git describe --tags --dirty --always".!!.stripPrefix("v").trim

sbtPlugin := true

scalacOptions ++= List("-unchecked")

publishMavenStyle := false
bintrayRepository := "sbt-plugins"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.5",
  "org.elasticsearch" % "elasticsearch" % "2.3.4"
)
