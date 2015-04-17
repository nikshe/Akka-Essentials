package org.akka.essentials.scala.future.example
import akka.actor.Actor

class OrderActor extends Actor {

  def receive = {
    case userId: Int =>
      Thread.sleep(2000)
      sender ! new Order(userId, 123, 345, 5)
  }
}