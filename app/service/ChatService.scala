package service

import actors.ChatServer

/**
  * Created by pozpl on 07.03.17.
  */
class ChatService (eventBus: ChatEventBus) extends ChatServer
    with SessionManagement
    with ChatRoutingService{

    override val chatEventBus: ChatEventBus = eventBus
//    override val storage: ActorRef = _
}

