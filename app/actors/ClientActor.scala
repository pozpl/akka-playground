package actors

import akka.actor.{Actor, ActorRef, Props}
import models.db.User
import play.api.libs.json.JsValue
import service.protocol._
import service.routing.ChatEventBus
import java.time.Instant
import java.time.ZoneOffset

import service.session.ChatService;

/**
  * Created by pozpl on 25.02.17.
  */
class ClientActor (user:User, out: ActorRef, chatService: ActorRef) extends ActorWithLogging {

    private var signedInUser : Option[User] = None


    chatService ! user

    def handleMessage(evt: ClientMessages): Unit = {
//        lazy val responseTimestamp = currentTime
        evt match {
//            case login: Login => chatService ! login
            case textMessage: TextMessage => {
                val timeStamp:Long = Instant.now().atOffset(ZoneOffset.UTC).toEpochSecond
                signedInUser.map((user:User) => {
                    chatService ! ReceivedTextMessage(textMessage, user, timeStamp)
                    out ! ClientMessages.clientMessage2JsValue(
                        OutboundTextMessage(user.userId.toString,
                            user.fullName.getOrElse(""),
                            textMessage.to,
                            textMessage.message, timeStamp))
                })
            }
            case getChatHistory: GetChatHistoryRequest => {
                log.info("Get chat history request " + getChatHistory.toString)
                signedInUser.map((user:User) => chatService ! ChatHistoryRequest(user.userId.toString, getChatHistory.chatCoordinate))
            }
        }
    }


    def receive = {
        case request: JsValue => {
            val response = handleMessage(request)
//            out ! response
        }
        case UserActivated(user) => {
            log.info("Authorised user " + user.userId + " " + user.email)
            signedInUser = Some(user)
        }
        case receivedMessage:ReceivedTextMessage => {
            log.info("Trying to send outbound message " + receivedMessage.textMessage.message)
            signedInUser.map((user:User) => out ! ClientMessages.clientMessage2JsValue(
                OutboundTextMessage(receivedMessage.sender.userId.toString,
                    receivedMessage.sender.fullName.getOrElse(""),
                    receivedMessage.textMessage.to,
                    receivedMessage.textMessage.message, receivedMessage.timeStump)
            ))
        }
        case chatHistoryResponse: ChatHistoryResponse => {
            log.info("Send chat history response")
            signedInUser.map((user:User) => out ! ClientMessages.clientMessage2JsValue(
                GetChatHistoryResponse(chatHistoryResponse.chatCoordinate, chatHistoryResponse.messages)
            ))
        }

    }
}

object ClientActor {
    def props(user: User, out: ActorRef, chatService: ActorRef) = {Props(new ClientActor(user, out, chatService ))}
}
