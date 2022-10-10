package com.betting.service

import com.betting.domain.{Bet, Protocols}
import com.betting.repository.BetRepository
import com.couchbase.client.scala.kv.{GetResult, MutationResult}

import scala.util.Try

object BetServiceImpl extends Protocols{
  def saveBet(bet: Bet): Try[MutationResult] = {
    BetRepository.saveBet(bet)
  }

  def getBet(betId: Long): Try[GetResult] = {
    BetRepository.getBet(betId)
  }
}
