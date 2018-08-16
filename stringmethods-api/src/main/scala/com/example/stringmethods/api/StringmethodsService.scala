package com.example.stringmethods.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

object StringmethodsService  {
  val TOPIC_NAME = "greetings"
}

/**
  * The StringMethods service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the StringmethodsService.
  */
trait StringmethodsService extends Service {


  /**
    * This gets published to Kafka.
    */
  def greetingsTopic(): Topic[GreetingMessageChanged]

  /**
    * Functions added*/

  def toUppercase: ServiceCall[String, String]
  def toLowercase: ServiceCall[String, String]
  def isEmpty(str: String): ServiceCall[NotUsed, Boolean]
  def areEqual(str1: String, str2: String): ServiceCall[NotUsed, Boolean]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("stringmethods")
      .withCalls(
        call(toUppercase),
        namedCall("toLowercase", toLowercase),
        pathCall("/isEmpty/:str", isEmpty _),
        restCall(Method.GET, "/areEqual/:one/another/:other", areEqual _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}

/**
  * The greeting message class.
  */
case class GreetingMessage(message: String)

object GreetingMessage {
  /**
    * Format for converting greeting messages to and from JSON.
    *
    * This will be picked up by a Lagom implicit conversion from Play's JSON format to Lagom's message serializer.
    */
  implicit val format: Format[GreetingMessage] = Json.format[GreetingMessage]
}



/**
  * The greeting message class used by the topic stream.
  * Different than [[GreetingMessage]], this message includes the name (id).
  */
case class GreetingMessageChanged(name: String, message: String)

object GreetingMessageChanged {
  /**
    * Format for converting greeting messages to and from JSON.
    *
    * This will be picked up by a Lagom implicit conversion from Play's JSON format to Lagom's message serializer.
    */
  implicit val format: Format[GreetingMessageChanged] = Json.format[GreetingMessageChanged]
}
