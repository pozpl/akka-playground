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
            if (getHistory.chatHistoryWithChatCoordinate.segment == ChatSegments.Individual &&
                getHistory.chatHistoryWithChatCoordinate.target.isDefined) {
                val pearUid = getHistory.chatHistoryWithChatCoordinate.target.get
                val initiatorUserUid = getHistory.initiatorUserUid
                val returnChatCoordinate = getHistory.chatHistoryWithChatCoordinate
                val historyResponse: Future[List[OutboundTextMessage]] = getChatHistory(pearUid, initiatorUserUid)
                historyResponse.map((messagesList) => {
                    senderRef ! ChatHistoryResponse(returnChatCoordinate, messagesList)
                })
            }
        }
    }

    private def getChatHistory(initiatorUserUid: String, pearUid: String): Future[List[OutboundTextMessage]] = {
        conversationsService.getTextMessagesForPrivateChat(initiatorUserUid, pearUid).map((list: List[ReceivedTextMessage]) => {
            list.map(receivedMessage => {
                OutboundTextMessage(receivedMessage.userUid, receivedMessage.textMessage.to, receivedMessage.textMessage.message)
            })
        })

    }
}