package models.db

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}


/**
  * The user object.
  *
  * @param userId    The unique ID of the user.
  * @param loginInfo The linked login info.
  * @param firstName Maybe the first name of the authenticated user.
  * @param lastName  Maybe the last name of the authenticated user.
  * @param fullName  Maybe the full name of the authenticated user.
  * @param email     Maybe the email of the authenticated provider.
  * @param avatarUrl Maybe the avatar URL of the authenticated provider.
  */
case class User(
    userId: UUID,
    loginInfo: LoginInfo,
    firstName: Option[String],
    lastName: Option[String],
    fullName: Option[String],
    email: Option[String],
    avatarUrl: Option[String]
) extends Identity



