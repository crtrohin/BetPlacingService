package com.betting.domain

import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json}
import spray.json.DefaultJsonProtocol

case class Bet(id: Long, stake: Long, selectionId: Long)

// Marshalling and unmarshalling
trait Protocols extends DefaultJsonProtocol {
  implicit val betFormat = jsonFormat3(Bet)
  implicit val betEncoder: Encoder[Bet] = bet => Json.obj(
    "id" -> bet.id.asJson,
    "stake" -> bet.stake.asJson,
    "selectionId" -> bet.selectionId.asJson
  )
}
