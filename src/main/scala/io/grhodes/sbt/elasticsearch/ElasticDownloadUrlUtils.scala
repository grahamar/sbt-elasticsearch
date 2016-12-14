package io.grhodes.sbt.elasticsearch

import java.net.{MalformedURLException, URL}

import org.apache.commons.lang3.StringUtils

private[elasticsearch] object ElasticDownloadUrlUtils {

  private val URLS = Map(
    "1." -> "https://download.elastic.co/elasticsearch/elasticsearch/elasticsearch-{VERSION}.zip",
    "2." -> "https://download.elasticsearch.org/elasticsearch/release/org/elasticsearch/distribution/zip/elasticsearch/{VERSION}/elasticsearch-{VERSION}.zip",
    "5." -> "https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-{VERSION}.zip"
  )

  def urlFromVersion(elasticVersion: String) = {
    val elsDownloadUrl = URLS.collectFirst {
      case (prefix, url) if elasticVersion.startsWith(prefix) => url
    }.getOrElse {
      throw new IllegalArgumentException("Invalid version: " + elasticVersion)
    }

    try {
      new URL(StringUtils.replace(elsDownloadUrl, "{VERSION}", elasticVersion))
    } catch {
      case e: MalformedURLException => throw new RuntimeException(e)
    }
  }

}
