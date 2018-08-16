package com.example.stringmethods.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.stringmethods.api.StringmethodsService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class StringmethodsLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new StringmethodsApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new StringmethodsApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[StringmethodsService])
}

abstract class StringmethodsApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[StringmethodsService](wire[StringmethodsServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = StringmethodsSerializerRegistry

  // Register the StringMethods persistent entity
  persistentEntityRegistry.register(wire[StringmethodsEntity])
}
