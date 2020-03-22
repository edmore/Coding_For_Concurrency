package com.example

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import Barista._
import CoffeeMachine._

// Barista companion object
object Barista {
  def props(coffeeMachineActor: ActorRef): Props = Props(new Barista(coffeeMachineActor))

  final case class WhatToMake(typeOfCoffee: String, number: Int)

  case object Brew

}

class Barista(machine: ActorRef) extends Actor {
  var order = "" // shared state

  def receive = {
    case WhatToMake(typeOfCoffee, number) =>
      order = number + ", " + typeOfCoffee // updates the "order" state
    case Brew =>
      machine ! MakeCoffee(order) // brew coffee
  }
}

// machine companion object
object CoffeeMachine {
  def props: Props = Props[CoffeeMachine]

  final case class MakeCoffee(order: String)

}

class CoffeeMachine extends Actor with ActorLogging {
  def receive = {
    case MakeCoffee(order) =>
      log.info("Brewing ... " + order) // prints order
  }
}

object Main extends App {
  // Create the 'CoffeeWorld' actor system
  val system: ActorSystem = ActorSystem("CoffeeWorld")

  // Create machine actor
  val machine: ActorRef = system.actorOf(CoffeeMachine.props, "Machine")
  // Create the 'barista' actor
  val barista: ActorRef =
    system.actorOf(Barista.props(machine), "Barista")

  // send messages to Barista from main
  barista ! WhatToMake("Espresso", 1)
  barista ! Brew

  barista ! WhatToMake("Flat White", 2)
  barista ! Brew
}
