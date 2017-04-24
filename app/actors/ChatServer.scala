package actors

import service.conversations.TConversationsService
import service.routing.ChatEventBus
import service.subscriptions.IndividualSubscriptionsService
import service.user.UserService

/**
  * Created by pozpl on 02.03.17.
  */
trait ChatServer extends ActorWithLogging {

    log.info("Chat server starting up")

    //    val storage: ActorRef
    val chatEventBus: ChatEventBus

    val conversationsService: TConversationsService

    val individualSubscriptionsService:IndividualSubscriptionsService

    val userService: UserService

    // actor message handler
    def receive: Receive = sessionManagement orElse chatManagement

    // abstract methods to be defined somewhere else
    protected def chatManagement: Receive

    protected def sessionManagement: Receive
}


