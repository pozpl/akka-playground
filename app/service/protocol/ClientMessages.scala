package service.protocol

import play.api.libs.json._
import play.api.libs.functional.syntax._
import service.routing.ChatCoordinate

/**
  * Created by pozpl on 02.03.17.
  */

abstract class ClientMessages

abstract class MessageObjectTypeAware(val MSG_TYPE: String)

object ClientMessages {

    implicit val loginFormatter: Format[Login] = (
        (__ \ "uid").format[String] and
            (__ \ "messageType").format[String]
        ) (Login.apply, unlift(Login.unapply))

    implicit val chatCoordinateFormatter: Format[ChatCoordinate] = (
        (__ \ "segment").format[String] ~
            (__ \ "target").formatNullable[String]
        ) (ChatCoordinate.apply, unlift(ChatCoordinate.unapply))

    implicit val textMessageFormatter: Format[TextMessage] = (
        (__ \ "to").format[ChatCoordinate] ~
            (__ \ "message").format[String] and
            (__ \ "messageType").format[String]
        ) (TextMessage.apply, unlift(TextMessage.unapply))

    implicit val outboundMessageFormatter: Format[OutboundTextMessage] = (
        (__ \ "from").format[String] ~
            (__ \ "chatCoordinate").format[ChatCoordinate] ~
            (__ \ "message").format[String] and
            (__ \ "messageType").format[String]
        ) (OutboundTextMessage.apply, unlift(OutboundTextMessage.unapply))

    implicit val unknownMessageFormatter: Format[UnknownMessage] =
            (__ \ "messageType").format[String].inmap(t => UnknownMessage(t), (u:UnknownMessage) => u.messageType)


    implicit def jsValue2ClientMessage(jsValue: JsValue): ClientMessages = {
        (jsValue \ "messageType").as[String] match {
            case Login.MSG_TYPE => jsValue.as[Login]
            case TextMessage.MSG_TYPE => jsValue.as[TextMessage]
            case messageType => UnknownMessage(messageType)
        }
    }

    implicit def clientMessage2JsValue(outboundMessage: ClientMessages): JsValue = {
        outboundMessage match {
            case outboundMessage: OutboundTextMessage => Json.toJson(outboundMessage)
            case _ => Json.toJson(UnknownMessage("unknown message"))
        }
    }


}

case class Login(userUid: String, messageType: String = Login.MSG_TYPE) extends ClientMessages

case class UnknownMessage(messageType: String = UnknownMessage.MSG_TYPE) extends ClientMessages

case class TextMessage(to: ChatCoordinate, message: String, messageType: String = TextMessage.MSG_TYPE) extends ClientMessages

case class OutboundTextMessage(from: String, chatCoordinate: ChatCoordinate, message: String, messageType: String = OutboundTextMessage.MSG_TYPE) extends ClientMessages

object Login extends MessageObjectTypeAware("login")

object UnknownMessage extends MessageObjectTypeAware("unknown")

object ErrorMessage extends MessageObjectTypeAware("error")

object TextMessage extends MessageObjectTypeAware("text_message")

object OutboundTextMessage extends MessageObjectTypeAware("outbound_text_message")



