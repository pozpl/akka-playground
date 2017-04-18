package service.user

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import dao.repository.UserDao
import models.db.User
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

/**
  * Handles actions to users.
  *
  * @param userDao The user DAO implementation.
  */
class UserServiceImpl @Inject() (userDao: UserDao) extends UserService {

    /**
      * Retrieves a user that matches the specified login info.
      *
      * @param loginInfo The login info to retrieve a user.
      * @return The retrieved user or None if no user could be retrieved for the given login info.
      */
    def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userDao.find(loginInfo)

    def list():Future[Seq[User]] = userDao.list()

    /**
      * Saves a user.
      *
      * @param user The user to save.
      * @return The saved user.
      */
    def save(user: User) = userDao.save(user)

    /**
      * Saves the social profile for a user.
      *
      * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
      *
      * @param profile The social profile to save.
      * @return The user for whom the profile was saved.
      */
    def save(profile: CommonSocialProfile) = {
        userDao.find(profile.loginInfo).flatMap( saveUserOpt(_, profile) )
    }

    private def saveUserOpt(userOpt: Option[User], profile: CommonSocialProfile): Future[User] = userOpt match {
        case Some(user) => // Update user with profile
            userDao.save(user.copy(
                firstName = profile.firstName,
                lastName = profile.lastName,
                fullName = profile.fullName,
                email = profile.email,
                avatarUrl = profile.avatarURL
            ))
        case None => // Insert a new user
            userDao.save(User(
                userId = UUID.randomUUID(),
                loginInfo = profile.loginInfo,
                firstName = profile.firstName,
                lastName = profile.lastName,
                fullName = profile.fullName,
                email = profile.email,
                avatarUrl = profile.avatarURL
            ))
    }
}
