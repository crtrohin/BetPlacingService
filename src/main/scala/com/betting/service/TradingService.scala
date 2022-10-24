package com.betting.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.betting.domain.{Event, EventProtocols}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

class TradingService(implicit val system: ActorSystem) extends EventProtocols with SprayJsonSupport {

    def getEvent(eventId: Long): Future[Event] = {
      val responseFuture = Http().singleRequest(
        HttpRequest(
          method = HttpMethods.GET,
          uri = s"http://localhost:8080/events/${eventId}"
        )
      )

      responseFuture.flatMap {
        case response: HttpResponse if response.status == StatusCodes.OK =>
          Unmarshal(response.entity).to[Event].flatMap{
            entity =>
              Future.successful(entity)
          }
        case response: HttpResponse if response.status == StatusCodes.NotFound =>
          Future.failed(new NoSuchElementException)
      }
    }
}
