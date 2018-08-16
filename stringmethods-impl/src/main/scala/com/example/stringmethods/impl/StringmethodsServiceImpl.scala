package com.example.stringmethods.impl

import akka.NotUsed
import com.example.stringmethods.api
import com.example.stringmethods.api.StringmethodsService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}
import scala.concurrent.Future
/**
  * Implementation of the StringmethodsService.
  */
class StringmethodsServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends StringmethodsService {


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(StringmethodsEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[StringmethodsEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }

  /**
    * Functions added*/
  override def toUppercase: ServiceCall[String, String] = ServiceCall { x =>
    Future.successful(x.toUpperCase)
  }

  override def toLowercase: ServiceCall[String, String] = ServiceCall { x =>
    Future.successful(x.toLowerCase)
  }

  override def isEmpty(str: String): ServiceCall[NotUsed, Boolean] = ServiceCall { _ =>
    Future.successful(str.isEmpty)
  }

  override def areEqual(str1: String, str2: String): ServiceCall[NotUsed, Boolean] = ServiceCall { _ =>
    Future.successful(str1==str2)
  }
}
