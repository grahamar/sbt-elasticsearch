package io.grhodes.sbt.elasticsearch

import java.net.URL

import io.grhodes.sbt.elasticsearch.ElasticsearchKeys.IndexConf
import org.apache.commons.io.FileUtils
import pl.allegro.tech.embeddedelasticsearch.{EmbeddedElastic, IndexSettings, PopularProperties}

class ElasticsearchServer(
  clusterName: String,
  version: String,
  downloadUrl: Option[String],
  httpPort: Int,
  tcpPort: Int,
  indexConf: Seq[IndexConf],
  plugins: Seq[String]
) {
  private val Instance = {
    val elastic = EmbeddedElastic.builder()
      .withElasticVersion(version)
      .withDownloadUrl(downloadUrl.map(new URL(_)).getOrElse(ElasticDownloadUrlUtils.urlFromVersion(version)))
      .withSetting(PopularProperties.HTTP_PORT, httpPort)
      .withSetting(PopularProperties.TRANSPORT_TCP_PORT, tcpPort)
      .withSetting(PopularProperties.CLUSTER_NAME, clusterName)


    indexConf.foreach { index =>
      val indexSettings = IndexSettings.builder()
      index.types.map { t =>
        indexSettings.withType(t.name, FileUtils.readFileToString(t.mappingJsonFile, "UTF-8"))
      }

      index.settingsJsonFile match {
        case Some(settingsFile) => elastic.withIndex(
          index.name,
          indexSettings.withSettings(FileUtils.readFileToString(settingsFile, "UTF-8")).build()
        )

        case None =>
          if(index.types.isEmpty) {
            elastic.withIndex(index.name)
          } else {
            elastic.withIndex(index.name, indexSettings.build())
          }
      }
    }

    plugins.foreach(elastic.withPlugin)

    elastic.build()
  }

  def start() = {
    Instance.start()
  }

  def stop() = {
    Instance.stop()
  }

}
