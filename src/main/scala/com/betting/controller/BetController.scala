package com.betting.controller

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives.{as, complete, entity, path}
import akka.http.scaladsl.server.Route
import com.betting.domain.{Bet, Protocols}
import com.betting.service.BetServiceImpl

trait BetController extends SprayJsonSupport with Protocols {
  val routes: Route = {
    path("bets") {
      entity(as[Bet]) {
        bet =>
          BetServiceImpl.placeBet(bet)
          complete(Bet(bet.id, bet.stake))
      }
    }
  }
}
