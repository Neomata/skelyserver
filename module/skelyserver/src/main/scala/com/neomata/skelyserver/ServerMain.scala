package com.neomata.skelyserver

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.RemoteAddress
import akka.stream.scaladsl.Sink
import com.neomata.skelyserver.relay.Access
import com.neomata.skelyserver.relay.Basis
import com.neomata.skelyserver.server.Router
import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

object ServerMain extends App with StrictLogging {
  val host = Try(args(0)).getOrElse("localhost")
  val port = Try(args(1).toInt).getOrElse(8080)
  val directory = Try(args(2)).getOrElse("")
  logger.info("Hosting at {}:{}", host, port)

  implicit val system: ActorSystem[Access] = ActorSystem(Basis(), "server")
  val router = new Router(host, port, directory)

  val bindingFuture = Http().newServerAt(host, port).connectionSource()

  val aftermath = bindingFuture.to(Sink.foreach { connection =>
    logger.info(s"Incoming connection from ${connection.remoteAddress} to server http://$host:$port/")

    connection.handleWithAsyncHandler(router.route(RemoteAddress(connection.remoteAddress)))
  }).run()
}
