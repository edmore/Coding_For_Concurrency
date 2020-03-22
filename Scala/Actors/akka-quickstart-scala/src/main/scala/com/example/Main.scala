package com.example

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import Barista._
import Printer._

// Barista companion object
object Barista {
  def props(printerActor: ActorRef): Props = Props(new Barista(printerActor))

  final case class WhatToMake(typeOfCoffee: String, number: Int)

  case object MakePayment

}

class Barista(printerActor: ActorRef) extends Actor {
  var order = "" // state

  def receive = {
    case WhatToMake(typeOfCoffee, number) =>
      order = number + ", " + typeOfCoffee // updates the "order" state
    case MakePayment =>
      printerActor ! PrintReceipt(order) // print receipt
  }
}

// printer companion object
object Printer {
  def props: Props = Props[Printer]

  final case class PrintReceipt(order: String)

}

class Printer extends Actor with ActorLogging { // extends logger as well
  def receive = {
    case PrintReceipt(order) =>
      log.info("Payment received (from " + sender() + "): " + order) // prints receipt
  }
}

object Main extends App {
  // Create the 'CoffeeWorld' actor system
  val system: ActorSystem = ActorSystem("CoffeeWorld")

  // Create printer actor
  val printer: ActorRef = system.actorOf(Printer.props, "printerActor")
  // Create the 'coffee drinker' actors
  val espressoDrinker: ActorRef =
    system.actorOf(Barista.props(printer), "espressoDrinker")
  val flatWhiteDrinker: ActorRef =
    system.actorOf(Barista.props(printer), "flatWhiteDrinker")

  // send messages
  espressoDrinker ! WhatToMake("Espresso", 1)
  espressoDrinker ! MakePayment

  flatWhiteDrinker ! WhatToMake("Flat White", 2)
  flatWhiteDrinker ! MakePayment
}
