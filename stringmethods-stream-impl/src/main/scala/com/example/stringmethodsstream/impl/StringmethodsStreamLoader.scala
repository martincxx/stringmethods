package com.example.stringmethodsstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.stringmethodsstream.api.StringmethodsStreamService
import com.example.stringmethods.api.StringmethodsService
import com.softwaremill.macwire._

class StringmethodsStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new StringmethodsStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new StringmethodsStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[StringmethodsStreamService])
}

abstract class StringmethodsStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[StringmethodsStreamService](wire[StringmethodsStreamServiceImpl])

  // Bind the StringmethodsService client
  lazy val stringmethodsService = serviceClient.implement[StringmethodsService]
}
