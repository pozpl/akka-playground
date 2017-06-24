package service.routing

import actors.ChatServer
import models.db.User
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
            if(msg.textMessage.to.segment == ChatSegments.Individual){
                msg.textMessage.to.target match {
                    case Some(userUid) => userService.find(userUid).map((userOpt: Option[User]) => {
                        userOpt match {
                            case Some(messageReceiver) => individualSubscriptionsService.isSubscribed(messageReceiver, msg.sender)
                                .map(if(_){
                                    chatEventBus.publish(msg)
                                })
                            case _ => None    
                        }
                    })
                    case _ => None
                }

            }else{
                chatEventBus.publish(msg)
            }

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
                OutboundTextMessage(receivedMessage.sender.userId.toString,
                    receivedMessage.sender.fullName.getOrElse(""),
                    receivedMessage.textMessage.to,
                    receivedMessage.textMessage.message, receivedMessage.timeStump)
            })
        })

    }
}