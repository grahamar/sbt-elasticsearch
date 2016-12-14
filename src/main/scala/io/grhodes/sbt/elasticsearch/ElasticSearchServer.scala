package io.grhodes.sbt.elasticsearch

import java.net.URL

import io.grhodes.sbt.elasticsearch.ElasticsearchKeys.IndexConf
import org.apache.commons.io.FileUtils
import pl.allegro.tech.embeddedelasticsearch.{EmbeddedElastic, IndexSettings, PopularProperties}

class ElasticsearchServer(
  clusterName: String,
  version: String,
  downloadUrl: Option[String],
  indexConf: Seq[IndexConf]
) {
  private val Instance = {
    val elastic = EmbeddedElastic.builder()
      .withElasticVersion(version)
      .withDownloadUrl(downloadUrl.map(new URL(_)).getOrElse(ElasticDownloadUrlUtils.urlFromVersion(version)))
      .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9300)
      .withSetting(PopularProperties.CLUSTER_NAME, clusterName)

    indexConf.foreach { index =>
      index.settingsJsonFile match {
        case Some(settingsFile) => elastic.withIndex(
          index.name,
          IndexSettings.builder().withSettings(FileUtils.readFileToString(settingsFile, "UTF-8")).build()
        )
        case None => elastic.withIndex(index.name)
      }
    }

    elastic.build()
  }

  def start() = {
    Instance.start()
  }

  def stop() = {
    Instance.stop()
  }

}
