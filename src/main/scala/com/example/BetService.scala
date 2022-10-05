package com.example

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import spray.json._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.{cors}

final case class Bet(id: Long, stake: Long)

trait BetJsonProtocol extends DefaultJsonProtocol {
  implicit val betFormat = jsonFormat2(Bet)
}

object BetService extends BetJsonProtocol with SprayJsonSupport{
  implicit val system = ActorSystem(Behaviors.empty, "akka-http")

  val placeBet = post {
    path("bet") {
      entity(as[Bet]) {
        bet =>
          println("The bet was placed!")
          complete(Bet(bet.id, bet.stake))
      }
    }
  }

  val route = cors() {
    concat(placeBet)
  }

  def main(args: Array[String]): Unit = {
    Http().newServerAt("localhost", 8080).bind(route)
  }
}
