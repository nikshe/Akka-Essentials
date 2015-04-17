package org.akka.essentials.scala.future.example
import akka.actor.Actor

class AddressActor extends Actor {

  def receive = {
    case userId: Int =>
      Thread.sleep(3000)
      sender ! new Address(userId, "Munish Gupta", "Sarjapura Road", "Bangalore, India")
  }
}