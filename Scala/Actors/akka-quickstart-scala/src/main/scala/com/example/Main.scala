package com.example

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import Barista._
import Printer._

// Barista companion object
object Barista {
  def props(printerActor: ActorRef): Props = Props(new Barista(printerActor))

  final case class WhatToMake(typeOfCoffee: String, number: Int)

  case object PrintOrder

}

class Barista(printerActor: ActorRef) extends Actor {
  var order = "" // shared state

  def receive = {
    case WhatToMake(typeOfCoffee, number) =>
      order = number + ", " + typeOfCoffee // updates the "order" state
    case PrintOrder =>
      printerActor ! Order(order) // print order
  }
}

// printer companion object
object Printer {
  def props: Props = Props[Printer]

  final case class Order(order: String)

}

class Printer extends Actor with ActorLogging {
  def receive = {
    case Order(order) =>
      log.info("Order: " + order) // prints order
  }
}

object Main extends App {
  // Create the 'CoffeeWorld' actor system
  val system: ActorSystem = ActorSystem("CoffeeWorld")

  // Create printer actor
  val printer: ActorRef = system.actorOf(Printer.props, "printerActor")
  // Create the 'barista' actor
  val barista: ActorRef =
    system.actorOf(Barista.props(printer), "Barista")

  // send messages to Barista from main class
  barista ! WhatToMake("Espresso", 1)
  barista ! PrintOrder

  barista ! WhatToMake("Flat White", 2)
  barista ! PrintOrder
}
