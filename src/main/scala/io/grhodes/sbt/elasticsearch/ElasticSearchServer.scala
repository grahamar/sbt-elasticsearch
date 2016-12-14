package io.grhodes.sbt.elasticsearch

import java.io.File

import org.apache.commons.io.FileUtils
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.node.NodeBuilder._
import sbt.Keys.TaskStreams

class ElasticSearchServer(clusterName: String, dataDir: File) {

  private val settings = Settings.settingsBuilder
    .put("path.data", dataDir.toString)
    .put("path.home", dataDir.getParentFile.toString)
    .put("cluster.name", clusterName)
    .build

  private lazy val node = nodeBuilder().settings(settings).build
  val client: Client = node.client

  def start(streams: TaskStreams): Unit = {
    streams.log.info("Starting Elasticsearch test node...")
    node.start()
    streams.log.info("Elasticsearch test node started!")
  }

  def stop(streams: TaskStreams): Unit = {
    streams.log.info("Stopping Elasticsearch test node...")
    node.close()

    try {
      FileUtils.forceDelete(dataDir)
    } catch {
      case e: Exception => // dataDir cleanup failed
    }
    streams.log.info("Elasticsearch test node stopped!")
  }

}
