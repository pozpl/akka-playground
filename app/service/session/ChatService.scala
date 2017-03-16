package service.session

import actors.ChatServer
import service.autorisation.AuthorizationService
import service.routing.{ChatEventBus, ChatRoutingService}

/**
  * Created by pozpl on 07.03.17.
  */
class ChatService (eventBus: ChatEventBus, authService: AuthorizationService) extends ChatServer
    with SessionManagement
    with ChatRoutingService{

    override val chatEventBus: ChatEventBus = eventBus
//    override val storage: ActorRef = _
    override val authorizationService: AuthorizationService = authService
}

