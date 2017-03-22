package actors

import akka.actor.{Actor, ActorRef}
import models.User
import play.api.libs.json.JsValue
import service.protocol._
import service.routing.ChatEventBus
import java.time.Instant;
import java.time.ZoneOffset;

/**
  * Created by pozpl on 25.02.17.
  */
class ClientActor (out: ActorRef, chatService: ActorRef, eventBus: ChatEventBus) extends ActorWithLogging {

    private var signedInUser : Option[User] = None


    def handleMessage(evt: ClientMessages): Unit = {
//        lazy val responseTimestamp = currentTime
        evt match {
            case login: Login => chatService ! login
            case textMessage: TextMessage => {
                val timeStump:Long = Instant.now().atOffset(ZoneOffset.UTC).toEpochSecond
                signedInUser.map((user:User) => chatService ! ReceivedTextMessage(textMessage, user.uid, timeStump))
            }
            case getChatHistory: GetChatHistoryRequest => {
                log.info("Get chat history request " + getChatHistory.toString)
                signedInUser.map((user:User) => chatService ! ChatHistoryRequest(user.uid, getChatHistory.chatCoordinate))
            }
        }
    }


//    def handleChatEvent(evt: ChatEvent):JsValue = {
//        ResponseMessage()
//    }
//    chat ! Protocol.Login

//    override def postStop() = chat ! Protocol.Logout

    def receive = {
        case request: JsValue => {
            val response = handleMessage(request)
//            out ! response
        }
        case UserActivated(user) => {
            log.info("Authorised user")
            signedInUser = Some(user)
        }
        case receivedMessage:ReceivedTextMessage => {
            log.info("Trying to send outbound message " + receivedMessage.textMessage.message)
            signedInUser.map((user:User) => out ! ClientMessages.clientMessage2JsValue(
                OutboundTextMessage(receivedMessage.userUid, receivedMessage.textMessage.to, receivedMessage.textMessage.message)
            ))
        }
        case chatHistoryResponse: ChatHistoryResponse => {
            log.info("Send chat history response")
            signedInUser.map((user:User) => out ! ClientMessages.clientMessage2JsValue(
                GetChatHistoryResponse(chatHistoryResponse.chatCoordinate, chatHistoryResponse.messages)
            ))
        }
        // this handles messages from the websocket
//        case ChatEvent(ChatCoordinate(segment, target), ChatMessage(from, message)) chatMessage: ChatMessage =>
//            chat ! ClientSentMessage(text)
//
//        case ClientSentMessage(text) =>
//            out ! text
    }
}




