package com.betting.service

import akka.Done
import com.betting.domain.Bet
import com.betting.repository.BetRepository

import scala.concurrent.Future

object BetServiceImpl {
  def saveBet(bet: Bet): Future[Done] = {
    BetRepository.saveBet(bet)
  }

  def getBet(betId: Long): Future[Option[Bet]] = {
    BetRepository.getBet(betId)
  }
}
