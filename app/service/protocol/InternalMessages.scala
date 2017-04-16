package service.protocol

import models.db.User
import service.routing.ChatCoordinate

/**
  * Created by pozpl on 02.03.17.
  */
abstract class InternalMessages

case class UserActivated(user: User) extends InternalMessages

case class ReceivedTextMessage(textMessage: TextMessage, userUid: String, timeStump: Long) extends InternalMessages

case class ChatHistoryRequest(initiatorUserUid: String, chatHistoryWithChatCoordinate: ChatCoordinate) extends InternalMessages
case class ChatHistoryResponse(chatCoordinate: ChatCoordinate ,messages: List[OutboundTextMessage]) extends InternalMessages
