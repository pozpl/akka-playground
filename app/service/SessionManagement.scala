package service

import actors.{ActorWithLogging, ChatServer}
import akka.actor.Terminated
import models.User
import service.protocol.{Login, UserActivated}

/**
  * Created by pozpl on 02.03.17.
  */
trait SessionManagement {

    this: ChatServer =>

//    val storage: ActorRef // needs someone to provide the ChatStorage


    protected def sessionManagement: Receive = {
        case Login(userUid, _) => {
            log.info("User [%s] has logged in".format(userUid))
            chatEventBus.subscribe(sender(), ChatCoordinate(ChatSegments.Individual, Some(userUid)))
            context.watch(sender())
            sender() ! UserActivated(User(userUid))
        }
        case Terminated(terminated) => {
            log.info("Unsubscribing actor %s".format(terminated))
            chatEventBus.unsubscribe(terminated)
        }
    }


}
