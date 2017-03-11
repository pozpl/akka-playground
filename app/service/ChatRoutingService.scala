package service

import actors.ChatServer
import service.protocol.ReceivedTextMessage

/**
  * Created by pozpl on 07.03.17.
  */
trait ChatRoutingService {
    this: ChatServer =>

    protected def chatManagement: Receive = {
        case msg:ReceivedTextMessage => chatEventBus.publish(msg)
    }
}
