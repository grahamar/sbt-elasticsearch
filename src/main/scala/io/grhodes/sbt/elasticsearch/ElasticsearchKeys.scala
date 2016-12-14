package io.grhodes.sbt.elasticsearch

import java.io.File

import sbt._

object ElasticsearchKeys {

  case class IndexConf(name: String, settingsJsonFile: File)

  lazy val elasticsearchClusterName = settingKey[String]("The name of the elasticsearch cluster. Defaults to 'elasticsearch'")
  lazy val elasticsearchUrl = settingKey[Option[String]]("The URL to download elasticsearch from.")
  lazy val elasticsearchVersion = settingKey[String]("The version of elasticsearch. Defaults to '2.3.4'")

  lazy val indexConf = settingKey[Seq[IndexConf]]("IndexConfs to initialize indexes on startup. No indexes are created by default. IndexConf(name, settingsJsonFile)")

  lazy val startElasticsearch = TaskKey[Unit]("start-elasticsearch")
  lazy val stopElasticsearch = TaskKey[Unit]("stop-elasticsearch")
  lazy val elasticsearchTestCleanup = TaskKey[Tests.Cleanup]("elasticsearch-test-cleanup")

  lazy val _elasticsearchServer = settingKey[ElasticsearchServer]("The elasticsearch server instance")
}
