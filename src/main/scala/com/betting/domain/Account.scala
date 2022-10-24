package com.betting.domain

import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json}
import spray.json.DefaultJsonProtocol

final case class Account(id: Long, name: String)

trait AccountProtocols extends DefaultJsonProtocol  {
  implicit val accountFormat = jsonFormat2(Account)
  implicit val accountEncoder: Encoder[Account] = account => Json.obj(
    "id" -> account.id.asJson,
    "name" -> account.name.asJson
  )}
