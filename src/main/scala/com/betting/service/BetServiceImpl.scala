package com.betting.service

import com.betting.domain.{Bet, BetProtocols}
import com.betting.repository.BetRepository
import com.couchbase.client.scala.kv.{GetResult, MutationResult}
import com.couchbase.client.scala.query.QueryResult

import scala.util.Try

object BetServiceImpl extends BetProtocols{
  def placeBet(bet: Bet): Try[MutationResult] = {
    BetRepository.placeBet(bet)
  }

  def getBet(betId: Long): Try[GetResult] = {
    BetRepository.getBet(betId)
  }

  def getBetByAccountId(accountId: Long): Try[QueryResult] = {
    BetRepository.getBetByAccountId(accountId)
  }

  def getBetByEventId(eventId: Long): Try[QueryResult] = {
    BetRepository.getBetByEventId(eventId)
  }
}
