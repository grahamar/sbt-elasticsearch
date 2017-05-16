package io.grhodes.sbt.elasticsearch

import sbt._
import sbt.Keys._
import ElasticsearchKeys._

object ElasticsearchPlugin extends AutoPlugin {

  override val trigger = allRequirements

  val autoImport = ElasticsearchKeys

  override val projectSettings = Seq(
    elasticsearchClusterName := "elasticsearch",
    elasticsearchVersion := "2.3.4",
    elasticsearchUrl := None,

    indexConf := Seq(),
    pluginConf := Seq(),

    _elasticsearchServer := new ElasticsearchServer(
      elasticsearchClusterName.value,
      elasticsearchVersion.value,
      elasticsearchUrl.value,
      indexConf.value,
      pluginConf.value
    ),

    startElasticsearch := {
      streams.value.log.info("Starting Elasticsearch...")
      _elasticsearchServer.value.start()
      ()
    },
    //if compilation of test classes fails, Elasticsearch should not be invoked. (moreover, Test.Cleanup won't execute to stop it...)
    startElasticsearch <<= startElasticsearch.dependsOn(compile in Test),
    stopElasticsearch := {
      streams.value.log.info("Stopping Elasticsearch...")
      _elasticsearchServer.value.stop()
      ()
    },

    elasticsearchTestCleanup := Tests.Cleanup(() => {
      streams.value.log.info("Stopping Elasticsearch...")
      _elasticsearchServer.value.stop()
    })
  )

}
