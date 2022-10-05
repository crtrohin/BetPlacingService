package com.betting.service

import com.betting.domain.Bet

object BetServiceImpl {
  def placeBet(bet: Bet): Unit = {
    println(s"The bet with id=${bet.id} and stake=${bet.stake} was placed!")
  }
}
