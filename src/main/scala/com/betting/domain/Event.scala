package com.betting.domain

import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Json}
import spray.json.DefaultJsonProtocol

final case class Event(
                        id: Long,
                        name: String,
                        inPlay: Boolean,
                        startTime: String,
                        endTime: String
                      )

trait EventProtocols extends DefaultJsonProtocol {
  implicit val eventFormat = jsonFormat5(Event)
  implicit val eventEncoder: Encoder[Event] = event => Json.obj(
    "id" -> event.id.asJson,
    "name" -> event.name.asJson,
    "inPlay" -> event.inPlay.asJson,
    "startTime" -> event.startTime.asJson,
    "endTime" -> event.endTime.asJson
  )

  implicit val eventDecoder :Decoder[Event] = event =>
    for {
      id <- event.get[Long]("id")
      name <- event.get[String]("name")
      inPlay <- event.get[Boolean]("inPlay")
      startTime <- event.get[String]("startTime")
      endTime <- event.get[String]("endTime")
    } yield Event(id, name, inPlay, startTime, endTime)
}

