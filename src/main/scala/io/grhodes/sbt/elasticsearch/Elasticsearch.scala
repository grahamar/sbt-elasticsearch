package io.grhodes.sbt.elasticsearch

import sbt._
import sbt.Keys._
import java.io.File
import java.nio.file.Files

object Elasticsearch extends AutoPlugin {

  object autoImport {
    val elasticsearchClusterName = settingKey[String]("Elasticsearch cluster name.")
    val elasticsearchDataDir = settingKey[File]("Elasticsearch data directory.")
    val startElasticsearch = taskKey[Unit]("start-elasticsearch")
    val stopElasticsearchAfterTests = settingKey[Boolean]("stop-elasticsearch-after-tests")
    val stopElasticsearch = taskKey[Unit]("stop-elasticsearch")
    val elasticSearchServer = settingKey[ElasticSearchServer]("Elasticsearch server")
  }

  import autoImport._

  override val projectSettings = Seq(
    elasticsearchClusterName := "elasticsearch",
    elasticsearchDataDir := Files.createTempDirectory("elasticsearch_data_").toFile,
    stopElasticsearchAfterTests := true,
    elasticSearchServer := new ElasticSearchServer(elasticsearchClusterName.value, elasticsearchDataDir.value),
    startElasticsearch := elasticSearchServer.value.start(streams.value),
    //if compilation of test classes fails, Elasticsearch should not be invoked. (moreover, Test.Cleanup won't execute to stop it...)
    startElasticsearch := startElasticsearch.dependsOn(compile in Test),
    stopElasticsearch := elasticSearchServer.value.stop(streams.value),
    //make sure to Stop Elasticsearch when tests are done.
    testOptions in Test += Tests.Cleanup(() => {
      if(stopElasticsearchAfterTests.value) elasticSearchServer.value.stop(streams.value)
    })
  )

}
