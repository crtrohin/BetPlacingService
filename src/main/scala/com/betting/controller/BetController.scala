package com.betting.controller

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, path}
import akka.http.scaladsl.server.Route
import com.betting.domain.{Bet, Protocols}
import akka.http.scaladsl.server.Directives._
import com.betting.service.BetServiceImpl

trait BetController extends SprayJsonSupport with Protocols {
  val routes: Route =
    concat(
      get {
        pathPrefix("bets" / LongNumber) { betId =>
          val maybeBet = BetServiceImpl.getBet(betId)

          onSuccess(maybeBet) {
            case Some(bet) => {
              println(s"Bet with id=${betId}: ${bet}")
              complete(bet)
            }
            case None => {
              println(s"Bet with id=${betId} was not found!")
              complete(StatusCodes.NotFound)
            }
          }
        }
      },
      post {
        path("bets") {
          entity(as[Bet]) { bet =>
            val saved = BetServiceImpl.saveBet(bet)
            onSuccess(saved) { _ =>
              println(s"Bet with id=${bet.id}, stake=${bet.stake}, selectionId=${bet.selectionId} was successfully created!")
              complete("Bet created!")
            }
          }
        }
      }
    )
}
