package actors

import akka.actor.{Actor, ActorRef}
import models.User
import play.api.libs.json.JsValue
import service.protocol._
import service.routing.ChatEventBus

/**
  * Created by pozpl on 25.02.17.
  */
class ClientActor (out: ActorRef, chatService: ActorRef, eventBus: ChatEventBus) extends ActorWithLogging {

    private var signedInUser : Option[User] = None


    def handleMessage(evt: ClientMessages): Unit = {
//        lazy val responseTimestamp = currentTime
        evt match {
            case login: Login => chatService ! login
            case textMessage: TextMessage => signedInUser.map((user:User) => chatService ! ReceivedTextMessage(textMessage, user.uid))
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
            signedInUser.map((user:User) => out ! ClientMessages.clientMessage2JsValue( OutboundTextMessage(receivedMessage.userUid, receivedMessage.textMessage.to, receivedMessage.textMessage.message)))
        }
        // this handles messages from the websocket
//        case ChatEvent(ChatCoordinate(segment, target), ChatMessage(from, message)) chatMessage: ChatMessage =>
//            chat ! ClientSentMessage(text)
//
//        case ClientSentMessage(text) =>
//            out ! text
    }
}




