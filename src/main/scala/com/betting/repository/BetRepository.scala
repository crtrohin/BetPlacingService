package com.betting.repository

import com.betting.components.CouchbaseConnectionImpl
import com.betting.domain.{Bet, Protocols}
import com.couchbase.client.scala.codec.JsonSerializer.CirceEncode
import com.couchbase.client.scala.kv.{GetResult, MutationResult}
import io.circe.syntax.EncoderOps

import scala.util.Try

object BetRepository extends Protocols {
  val db = new CouchbaseConnectionImpl

  def getBet(id: Long): Try[GetResult] = {
    db.collection.get(id.toString)
  }

  def saveBet(bet: Bet): Try[MutationResult] = {
    val docId = bet.id.toString
    db.collection.upsert(docId, bet.asJson)(CirceEncode)
  }
}
