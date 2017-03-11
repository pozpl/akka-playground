package service

import actors.{ActorWithLogging, ChatServer}
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
            sender() ! UserActivated(User(userUid))
        }
    }


}
