//#full-example
package com.example

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }

//#greeter-companion
//#greeter-messages
object Greeter {
  //#greeter-messages
  def props(message: String, printerActor: ActorRef): Props = Props(new Greeter(message, printerActor)) // defines how to create an actor, configuration class / constructor
  //#greeter-messages
  // greeter messages should be immutable as they are shared between different threads
  final case class WhoToGreet(who: String)
  case object Greet
}
//#greeter-messages
//#greeter-companion

//#greeter-actor
// actor in a suspended state when not processing messages
// messages go into a mailbox which is essentially a message queue
class Greeter(message: String, printerActor: ActorRef) extends Actor { //extends the Actor trait and implements the receive method
  import Greeter._
  import Printer._

  var greeting = "" // actors state

  // expected messages
  // The order of multiple messages sent from the "same" Actor is preserved
  def receive = {
    case WhoToGreet(who) =>
      greeting = message + ", " + who // updates the "greeting" state
    case Greet           =>
      //#greeter-send-message
      printerActor ! Greeting(greeting) // sending a greeting to the Printer Actor
      //#greeter-send-message
  }
}
//#greeter-actor

//#printer-companion
//#printer-messages
object Printer {
  //#printer-messages
  def props: Props = Props[Printer] // defines how to create a printer
  //#printer-messages
  final case class Greeting(greeting: String) // defines the message that the actor expects
}
//#printer-messages
//#printer-companion

//#printer-actor
class Printer extends Actor with ActorLogging { // extends logger as well
  import Printer._

  def receive = {
    case Greeting(greeting) =>
      log.info("Greeting received (from " + sender() + "): " + greeting) // logs greeting
  }
}
//#printer-actor

//#main-class
object AkkaQuickstart extends App {
  import Greeter._

  // Create the 'helloAkka' actor system; container for actors
  val system: ActorSystem = ActorSystem("helloAkka")

  //#create-actors
  // Create the printer actor using the actorOf factory
  val printer: ActorRef = system.actorOf(Printer.props, "printerActor") // an actor reference

  // Create the 'greeter' actors
  val howdyGreeter: ActorRef =
    system.actorOf(Greeter.props("Howdy", printer), "howdyGreeter") // names used for lookups are important
  val helloGreeter: ActorRef =
    system.actorOf(Greeter.props("Hello", printer), "helloGreeter")
  val goodDayGreeter: ActorRef =
    system.actorOf(Greeter.props("Good day", printer), "goodDayGreeter")
  //#create-actors

  //#main-send-messages
  howdyGreeter ! WhoToGreet("Akka") // main sends message to howdyGreeter
  howdyGreeter ! Greet

  howdyGreeter ! WhoToGreet("Lightbend")
  howdyGreeter ! Greet

  helloGreeter ! WhoToGreet("Scala")
  helloGreeter ! Greet

  goodDayGreeter ! WhoToGreet("Play")
  goodDayGreeter ! Greet
  //#main-send-messages
}
//#main-class
//#full-example
