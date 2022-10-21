package com.betting.domain

import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json}
import spray.json.DefaultJsonProtocol

case class Bet(betId: Long, stake: Long, selectionId: Long, accountId: Long)

trait BetProtocols extends DefaultJsonProtocol {
  implicit val betFormat = jsonFormat4(Bet)
  implicit val betEncoder: Encoder[Bet] = bet => Json.obj(
    "betId" -> bet.betId.asJson,
    "stake" -> bet.stake.asJson,
    "selectionId" -> bet.selectionId.asJson,
    "accountId" -> bet.accountId.asJson)
}
