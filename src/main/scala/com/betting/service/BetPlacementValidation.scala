package com.betting.service

import akka.actor.ActorSystem
import com.betting.domain.{Bet, Event}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.{Failure, Success}

class BetPlacementValidation(implicit val system: ActorSystem) {
  val tradingService = new TradingService()

  def checkIfExistsAndIsActive(bet: Bet): Boolean = {
    val eventString: Future[Event] = tradingService.getEvent(bet.eventId)

    var existsAndIsActive = false
    eventString.onComplete {
      case Success(event) =>
        println(s"The event linked to the bet is: ${event}")
        existsAndIsActive = event.inPlay
      case Failure(_) =>
        println("The event linked to the bet doesn't exist.")
    }

    existsAndIsActive
  }

  def isStakeInRange(bet: Bet): Boolean = {
    10 to 10000 contains bet.stake
  }

}
