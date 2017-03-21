package actors

import akka.actor.ActorRef
import service.autorisation.AuthorizationService
import service.conversations.TConversationsService
import service.routing.ChatEventBus

/**
  * Created by pozpl on 02.03.17.
  */
trait ChatServer extends ActorWithLogging {

    log.info("Chat server starting up")

    //    val storage: ActorRef
    val chatEventBus: ChatEventBus

    val authorizationService : AuthorizationService

    val conversationsService: TConversationsService

    // actor message handler
    def receive: Receive = sessionManagement orElse chatManagement

    // abstract methods to be defined somewhere else
    protected def chatManagement: Receive

    protected def sessionManagement: Receive
}


