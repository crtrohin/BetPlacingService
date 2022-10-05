package com.betting

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.betting.controller.BetController

import scala.util.{Failure, Success}

object BetApplication extends App with BetController {
  implicit val system = ActorSystem()
  import system.dispatcher

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes)

  bindingFuture.onComplete {
    case Success(binding) => {
      val address = binding.localAddress
      println(s"Placing bet online at server ${address.getHostString}:${address.getPort}")
    }
    case Failure(ex) => {
      println("Failed to bind to endpoint, terminating system", ex)
      system.terminate()
    }
  }
}
