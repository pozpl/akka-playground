package service.session

import actors.ChatServer
import service.conversations.TConversationsService
import service.routing.{ChatEventBus, ChatRoutingService}
import service.subscriptions.IndividualSubscriptionsService
import service.user.UserService

/**
  * Created by pozpl on 07.03.17.
  */
class ChatService (eventBus: ChatEventBus, conversationServiceImpl: TConversationsService, val userService: UserService,
                   val individualSubscriptionsService: IndividualSubscriptionsService) extends ChatServer
    with SessionManagement
    with ChatRoutingService{

    override val chatEventBus: ChatEventBus = eventBus
    override val conversationsService: TConversationsService = conversationServiceImpl
}

