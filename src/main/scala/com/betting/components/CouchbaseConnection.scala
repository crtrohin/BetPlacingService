package com.betting.components

import com.betting.domain.Protocols
import com.couchbase.client.scala.{Bucket, Cluster, Collection, Scope}

trait CouchbaseConnection {
  val cluster: Cluster
  val bucket: Bucket
  val collection: Collection
}

class CouchbaseConnectionImpl extends Protocols with CouchbaseConnection {
  val cluster: Cluster = Cluster
    .connect("127.0.0.1", "username", "password")
    .get

  val bucket: Bucket = cluster.bucket("placedBets")
  val scope: Scope = bucket.defaultScope;
  val collection: Collection = scope.collection("bets");

  println("Successfully opened cluster")
}
