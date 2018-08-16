package com.example.stringmethodsstream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * The StringMethods stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the StringmethodsStream service.
  */
trait StringmethodsStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor = {
    import Service._

    named("stringmethods-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

