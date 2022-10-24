package com.betting.domain

import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json}
import spray.json.DefaultJsonProtocol

case class Bet(id: Long, stake: Long, eventId: Long, accountId: Long)

trait BetProtocols extends DefaultJsonProtocol {
  implicit val betFormat = jsonFormat4(Bet)
  implicit val betEncoder: Encoder[Bet] = bet => Json.obj(
    "id" -> bet.id.asJson,
    "stake" -> bet.stake.asJson,
    "eventId" -> bet.eventId.asJson,
    "accountId" -> bet.accountId.asJson)
}


object BetProtocols extends BetProtocols