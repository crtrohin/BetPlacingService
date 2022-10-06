package com.betting.repository

import akka.Done
import com.betting.BetApplication.system
import com.betting.domain.Bet

import scala.concurrent.Future

object BetRepository {
  import system.dispatcher

  var bets: List[Bet] = Nil

  def getBet(id: Long): Future[Option[Bet]] = Future {
    bets.find(b => b.id == id)
  }

  def saveBet(bet: Bet): Future[Done] = {
    bets = bets :+ bet
    Future {Done}
  }
}
