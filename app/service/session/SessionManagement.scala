package service.session

import actors.ChatServer
import akka.actor.{ActorRef, Terminated}
import akka.pattern.pipe
import models.User
import service.protocol.{Login, UserActivated}
import service.routing.{ChatCoordinate, ChatSegments}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by pozpl on 02.03.17.
  */
trait SessionManagement {

    this: ChatServer =>

    import scala.concurrent.ExecutionContext.Implicits.global

    //    val storage: ActorRef // needs someone to provide the ChatStorage

    protected def sessionManagement: Receive = {
        case Login(userUid, _) => {
            log.info("User [%s] wants to log in".format(userUid))
            val senderRef  = sender();
            authorizationService.isUserExists(userUid)
                .map((isAuthorised: Boolean) => {
                    if (isAuthorised) {
                        SessionManagementUserAuthorized(userUid, senderRef)
                    }  else{
                        SessionManagementUserUnAuthorized(userUid)
                    }
                }) pipeTo(self)
        }
        case SessionManagementUserAuthorized(userUid, userActor) => {
            log.info("User [%s] was unproved to logged in".format(userUid))
            log.info("User actor is [%s]".format(userActor))
            chatEventBus.subscribe(userActor, ChatCoordinate(ChatSegments.Individual, Some(userUid)))
            context.watch(userActor)
            userActor ! UserActivated(User(userUid))
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
