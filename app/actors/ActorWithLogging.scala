package actors

import akka.actor.Actor
import akka.event.Logging

/**
  * Created by pozpl on 28.02.17.
  */
trait ActorWithLogging extends Actor{

    val log = Logging(context.system, this)


}
