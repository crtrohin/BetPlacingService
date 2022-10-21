package com.betting.controller

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, path}
import akka.http.scaladsl.server.Route
import com.betting.domain.{Bet, BetProtocols}
import akka.http.scaladsl.server.Directives._
import com.betting.service.BetServiceImpl
import com.couchbase.client.scala.json.JsonObjectSafe
import com.couchbase.client.scala.kv.MutationResult
import com.couchbase.client.scala.query.QueryResult
import com.couchbase.client.scala.json._
import scala.util.{Failure, Success, Try}

trait BetController extends BetProtocols with SprayJsonSupport {
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
      get {
        pathPrefix("accounts" / LongNumber / "bets") { accountId =>
          val foundBets: Try[QueryResult] = BetServiceImpl.getBetByAccountId(accountId)
          foundBets match {
            case Success(value) =>
              value.rowsAs[JsonObject] match {
                case Success(row) if (row.size != 0) =>
                  println(s"Bets placed by account with id=${accountId} are: ")
                  row.toArray.map(x => x.toString()).map(x => println(x))
                  complete("Success")
                case Success(row) if (row.size == 0) =>
                  println(s"No bets were found for account with id=${accountId}")
                  complete(s"No bets were found for account with id=${accountId}")
                case Failure(ex:Exception) =>
                  println(s"Exception: ${ex}")
                  complete("Failure")
              }
            case Failure(ex: Exception) =>
              println(s"Exception: ${ex}")
              complete("Failure")
          }
        }
      },
      post {
        path("bets") {
          entity(as[Bet]) { bet =>
            val savedBet: Try[MutationResult] = BetServiceImpl.placeBet(bet)
            savedBet match {
              case Success(_) =>
                println(s"Bet with id=${bet.betId}, stake=${bet.stake}, selectionId=${bet.selectionId} was created!")
                complete("Bet created!")
              case Failure(exception) =>
                complete("Error: " + exception)
            }
          }
        }
      }
    )
}
