package com.betting.controller

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, path}
import akka.http.scaladsl.server.Route
import com.betting.domain.{Bet, Protocols}
import akka.http.scaladsl.server.Directives._
import com.betting.service.BetServiceImpl
import com.couchbase.client.scala.json.JsonObjectSafe
import com.couchbase.client.scala.kv.MutationResult

import scala.util.{Failure, Success, Try}

trait BetController extends SprayJsonSupport with Protocols {
  val routes: Route =
    concat(
      get {
        pathPrefix("bets" / LongNumber) { betId =>
          val foundBet = BetServiceImpl.getBet(betId)
          foundBet match {
            case Success(result) => {
              result.contentAs[JsonObjectSafe] match {
                case Success(json) =>
                  println(s"Bet with id=${betId}:${json}")
                  complete(s"Bet with id=${betId}:${json}")
                case Failure(_) =>
                  println(s"Bet with id=${betId} was not found!")
                  complete(StatusCodes.NotFound)
              }
            }
            case Failure(_) => {
              println(s"Bet with id=${betId} was not found!")
              complete(s"Bet with id=${betId} was not found!")
            }
          }
        }
      },
      post {
        path("bets") {
          entity(as[Bet]) { bet =>
            val savedBet: Try[MutationResult] = BetServiceImpl.saveBet(bet)
            savedBet match {
              case Success(_) =>
                println(s"Bet with id=${bet.id}, stake=${bet.stake} and selectionId=${bet.selectionId} was created!")
                complete("Bet created!")
              case Failure(exception) =>
                complete("Error: " + exception)
            }
          }
        }
      }
    )
}
