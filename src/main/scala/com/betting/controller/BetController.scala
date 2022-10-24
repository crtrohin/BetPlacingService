package com.betting.controller

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ResponseEntity, StatusCodes}
import akka.http.scaladsl.server.Directives.{as, complete, entity, path}
import akka.http.scaladsl.server.Route
import com.betting.domain.{Bet, BetProtocols, Event}
import akka.http.scaladsl.server.Directives._
import com.betting.service.{BetServiceImpl, TradingService}
import com.couchbase.client.scala.json.JsonObjectSafe
import com.couchbase.client.scala.kv.MutationResult
import com.couchbase.client.scala.query.QueryResult
import com.couchbase.client.scala.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class BetController(implicit val system: ActorSystem) extends BetProtocols with SprayJsonSupport {
  val tradingService = new TradingService()
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
      },get {
        pathPrefix("events" / LongNumber / "bets") { eventId =>
          val foundBets: Try[QueryResult] = BetServiceImpl.getBetByEventId(eventId)
          foundBets match {
            case Success(value) =>
              value.rowsAs[JsonObject] match {
                case Success(row) if (row.size != 0) =>
                  println(s"Bets of the event with id=${eventId} are: ")
                  row.toArray.map(x => x.toString()).map(x => println(x))
                  complete("Success")
                case Success(row) if (row.size == 0) =>
                  println(s"No bets were found for event with id=${eventId}")
                  complete(s"No bets were found for event with id=${eventId}")
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
        pathPrefix("bets") {
          entity(as[Bet]) { bet =>
            val eventString: Future[Event] = tradingService.getEvent(bet.eventId)
            eventString.onComplete { x =>
              x match {
                case Success(event) =>
                  println(s"Event is: ${eventString}")
                  complete(s"Event is: ${eventString}")
                case Failure(ex) =>
                  println("Failure")
                  complete("Failure")
              }
            }

            val savedBet: Try[MutationResult] = BetServiceImpl.placeBet(bet)
            savedBet match {
              case Success(_) =>
                println(s"Bet with id=${bet.id}, stake=${bet.stake}, eventId=${bet.eventId} was created!")
                complete("Bet created!")
              case Failure(exception) =>
                complete("Error: " + exception)
            }

          }
        }
      }
    )
}
