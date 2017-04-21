package models.db

/**
  * Created by pozpl on 21.04.17.
  */
case class UserIndividualSubscription(id:Option[Int], userUid: String, subscribedUserUid: String, position: Int);
