package service.protocol

import models.User

/**
  * Created by pozpl on 02.03.17.
  */
abstract class InternalMessages

case class UserActivated(user: User) extends InternalMessages

case class ReceivedTextMessage(textMessage: TextMessage, userUid: String) extends InternalMessages
