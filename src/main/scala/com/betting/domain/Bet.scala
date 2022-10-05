package com.betting.domain

import spray.json.DefaultJsonProtocol

final case class Bet(id: Long, stake: Long)

// Marshalling and unmarshalling
trait Protocols extends DefaultJsonProtocol {
  implicit val BetFormat = jsonFormat2(Bet)
}
