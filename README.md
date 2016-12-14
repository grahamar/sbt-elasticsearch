sbt-elasticsearch
===============

Support for running Elasticsearch for use in integration tests.

Installation
------------
Add the following to your `project/plugins.sbt` file:

```
resolvers += Resolver.url("io.grhodes.sbt", url("https://dl.bintray.com/grahamar/sbt-plugins/"))(Resolver.ivyStylePatterns)

addSbtPlugin("io.grhodes.sbt" % "sbt-elasticsearch" % "0.0.1")
```

Configuration
-------------
The following represents the minimum amount of code required in a `build.sbt` to use [sbt-elasticsearch](https://github.com/grahamar/sbt-elasticsearch)

To use the elasticsearch in your project have your tests depend on starting the Elasticsearch server.

```
lazy val root = (project in file(".")).enablePlugins(Elasticsearch)

test in Test <<= (test in Test).dependsOn(startElasticsearch)
```

To specify the cluster-name (defaults to "elasticsearch")

```
elasticsearchClusterName := "my-test-cluster"
```

To change the data directory (defaults to a temporary directory)

```
elasticsearchDataDir := target.value / "elasticsearch"
```
