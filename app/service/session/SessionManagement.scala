package service.session

import actors.ChatServer
import akka.actor.{ActorRef, Terminated}
import models.db.User
import service.protocol.UserActivated
import service.routing.{ChatCoordinate, ChatSegments}

/**
  * Created by pozpl on 02.03.17.
  */
trait SessionManagement {

    this: ChatServer =>

    protected def sessionManagement: Receive = {
        case user:User => {
            log.info("User [%s] wants to log in".format(user.userId))
            val userActorRef  = sender()
            chatEventBus.subscribe(userActorRef, ChatCoordinate(ChatSegments.Individual, Some(user.userId.toString)))
            context.watch(userActorRef)
            userActorRef ! UserActivated(user)
        }
        case Terminated(terminated) => {
            log.info("Unsubscribing actor %s".format(terminated))
            chatEventBus.unsubscribe(terminated)
        }
    }


}

sealed trait SessionManagementInternalMessage
case class SessionManagementUserAuthorized(userUid:String, userActor: ActorRef) extends SessionManagementInternalMessage;
case class SessionManagementUserUnAuthorized(userUid:String) extends SessionManagementInternalMessage;
