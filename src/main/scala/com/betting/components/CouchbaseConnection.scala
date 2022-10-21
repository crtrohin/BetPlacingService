package com.betting.components

import com.betting.domain.{BetProtocols}
import com.couchbase.client.scala.{Bucket, Cluster, Collection, Scope}

trait CouchbaseConnection {
  val cluster: Cluster
  val bucket: Bucket
}

class CouchbaseConnectionImpl extends BetProtocols with CouchbaseConnection {
  val cluster: Cluster = Cluster
    .connect("127.0.0.1", "username", "password")
    .get

  val bucket: Bucket = cluster.bucket("placedBets")
  val scope: Scope = bucket.defaultScope;
  val betsCollection: Collection = scope.collection("bets");
  val accountsCollection: Collection = scope.collection("accounts");

  println("Successfully opened cluster")
}
