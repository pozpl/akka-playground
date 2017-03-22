package service.routing

import actors.ChatServer
import service.protocol.{ChatHistoryRequest, ChatHistoryResponse, OutboundTextMessage, ReceivedTextMessage}

import scala.concurrent.Future

/**
  * Created by pozpl on 07.03.17.
  */
trait ChatRoutingService {
    this: ChatServer =>

    import scala.concurrent.ExecutionContext.Implicits.global

    protected def chatManagement: Receive = {
        case msg: ReceivedTextMessage => {
            conversationsService.registerTextMessage(msg)
            chatEventBus.publish(msg)
        }
        case getHistory: ChatHistoryRequest => {
            val senderRef = sender()
            if (getHistory.chatHistoryWithChatCoordinate.segment == ChatSegments.Individual) {
                val returnChatCoordinate = getHistory.chatHistoryWithChatCoordinate
                val historyResponse: Option[Future[List[OutboundTextMessage]]] = getChatHistory(getHistory)
                historyResponse.map(_.map((messagesList) => {
                    senderRef ! ChatHistoryResponse(returnChatCoordinate, messagesList)
                }))
            }
        }
    }

    private def getChatHistory(getHistory: ChatHistoryRequest): Option[Future[List[OutboundTextMessage]]] = {
        val pearUidOpt = getHistory.chatHistoryWithChatCoordinate.target
        pearUidOpt.map(pearUid => {
            conversationsService.getTextMessagesForPrivateChat(getHistory.initiatorUserUid, pearUid)
                .map((list: List[ReceivedTextMessage]) => {
                    list.map(receivedMessage => {
                        OutboundTextMessage(receivedMessage.userUid, receivedMessage.textMessage.to, receivedMessage.textMessage.message)
                    })
                })
        })
    }
}