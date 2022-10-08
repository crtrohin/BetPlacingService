package com.betting

import com.couchbase.client.scala.Cluster

import scala.util.{Failure, Success, Try}

object CouchbaseConnection extends App {

  val clusterTry: Try[Cluster] =
    Cluster.connect("127.0.0.1", "username", "password")

  clusterTry match {

    case Success(cluster) =>
      val bucket = cluster.bucket("placedBets")
      val scope = bucket.defaultScope;
      val col = scope.collection("bets");

      println("Successfully opened cluster")
      cluster.disconnect()

    case Failure(err) =>
      println(s"Failed to open cluster: $err")
  }
}
