package io.grhodes.sbt.elasticsearch

import java.net.URL

import io.grhodes.sbt.elasticsearch.ElasticsearchKeys.IndexConf
import pl.allegro.tech.embeddedelasticsearch.{EmbeddedElastic, PopularProperties}

class ElasticsearchServer(
  clusterName: String,
  version: String,
  downloadUrl: Option[String],
  indexConf: Seq[IndexConf]
) {

  private val Instance = EmbeddedElastic.builder()
    .withElasticVersion(version)
    .withDownloadUrl(downloadUrl.map(new URL(_)).getOrElse(ElasticDownloadUrlUtils.urlFromVersion(version)))
    .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9300)
    .withSetting(PopularProperties.CLUSTER_NAME, clusterName)
    .build()

  def start() = {
    Instance.start()
  }

  def stop() = {
    Instance.stop()
  }

}
