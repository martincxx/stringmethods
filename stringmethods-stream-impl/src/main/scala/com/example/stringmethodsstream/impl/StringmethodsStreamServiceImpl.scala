package com.example.stringmethodsstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.example.stringmethodsstream.api.StringmethodsStreamService
import com.example.stringmethods.api.StringmethodsService

import scala.concurrent.Future

/**
  * Implementation of the StringmethodsStreamService.
  */
class StringmethodsStreamServiceImpl(stringmethodsService: StringmethodsService) extends StringmethodsStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(stringmethodsService.hello(_).invoke()))
  }
}
