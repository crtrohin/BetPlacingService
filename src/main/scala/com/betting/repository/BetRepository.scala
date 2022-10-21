package com.betting.repository

import com.betting.components.CouchbaseConnectionImpl
import com.betting.domain.{Bet, BetProtocols}
import com.couchbase.client.scala.codec.JsonSerializer.CirceEncode
import com.couchbase.client.scala.kv.{GetResult, MutationResult}
import com.couchbase.client.scala.query.{QueryOptions, QueryParameters, QueryResult}
import io.circe.syntax.EncoderOps

import scala.util.Try

object BetRepository extends BetProtocols {
  val db = new CouchbaseConnectionImpl

  def getBet(id: Long): Try[GetResult] = {
    db.betsCollection.get(id.toString)
  }

  def placeBet(bet: Bet): Try[MutationResult] = {
    val docId = bet.betId.toString
    db.betsCollection.upsert(docId, bet.asJson)
  }

  def getBetByAccountId(accountId: Long): Try[QueryResult] = {
    val stmt = """SELECT * FROM default:placedBets._default.bets bet
                 |WHERE bet.accountId = $accountId;""".stripMargin
    db.cluster.query(
      stmt,
      QueryOptions().parameters(
        QueryParameters.Named(Map("accountId" -> accountId))
      )
    )
  }
}
