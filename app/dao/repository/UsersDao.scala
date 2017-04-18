package dao.repository


import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import models.db.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Created by pozpl on 03.04.17.
  */
/**
  * Give access to the user object.
  */
trait UserDao {

    /**
      * Finds a user by its login info.
      *
      * @param loginInfo The login info of the user to find.
      * @return The found user or None if no user for the given login info could be found.
      */
    def find(loginInfo: LoginInfo): Future[Option[User]]

    /**
      * Get list of all users currently presented in the system
      *
      * @return
      */
    def list(): Future[Seq[User]]

    /**
      * Finds a user by its user ID.
      *
      * @param userID The ID of the user to find.
      * @return The found user or None if no user for the given ID could be found.
      */
    def find(userID: UUID): Future[Option[User]]

    /**
      * Saves a user.
      *
      * @param user The user to save.
      * @return The saved user.
      */
    def save(user: User): Future[User]
}


/**
  * Give access to the user object using Slick
  */
class UserDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UserDao with DaoSlick {

    import driver.api._

    /**
      * Finds a user by its login info.
      *
      * @param loginInfo The login info of the user to find.
      * @return The found user or None if no user for the given login info could be found.
      */
    def find(loginInfo: LoginInfo): Future[Option[User]] = {
        val userQuery = for {
            dbLoginInfo <- loginInfoQuery(loginInfo)
            dbUserLoginInfo <- slickUserLoginInfos.filter(_.loginInfoId === dbLoginInfo.id)
            dbUser <- slickUsers.filter(_.id === dbUserLoginInfo.userId)
        } yield dbUser
        db.run(userQuery.result.headOption).map { dbUserOption =>
            dbUserOption.map { user =>
                User(UUID.fromString(user.userId), loginInfo, user.firstName, user.lastName, user.fullName, user.email, user.avatarUrl)
            }
        }
    }

    /**
      * Get list of all users currently presented in the system
      *
      * @return
      */
    override def list(): Future[Seq[User]] = {
        val query = for {
            dbUser <- slickUsers
            dbUserLoginInfo <- slickUserLoginInfos.filter(_.userId === dbUser.id)
            dbLoginInfo <- slickLoginInfos.filter(_.id === dbUserLoginInfo.loginInfoId)
        } yield (dbUser, dbLoginInfo)
        db.run(query.result).map { (resultSeq: Seq[(DBUser, DBLoginInfo)]) =>
            resultSeq.map {
                case (user, loginInfo) =>
                    User(
                        UUID.fromString(user.userId),
                        LoginInfo(loginInfo.providerId, loginInfo.providerKey),
                        user.firstName,
                        user.lastName,
                        user.fullName,
                        user.email,
                        user.avatarUrl)
            }
        }
    }


    /**
      * Finds a user by its user ID.
      *
      * @param userID The ID of the user to find.
      * @return The found user or None if no user for the given ID could be found.
      */
    def find(userID: UUID) = {
        val query = for {
            dbUser <- slickUsers.filter(_.id === userID.toString)
            dbUserLoginInfo <- slickUserLoginInfos.filter(_.userId === dbUser.id)
            dbLoginInfo <- slickLoginInfos.filter(_.id === dbUserLoginInfo.loginInfoId)
        } yield (dbUser, dbLoginInfo)
        db.run(query.result.headOption).map { resultOption =>
            resultOption.map {
                case (user, loginInfo) =>
                    User(
                        UUID.fromString(user.userId),
                        LoginInfo(loginInfo.providerId, loginInfo.providerKey),
                        user.firstName,
                        user.lastName,
                        user.fullName,
                        user.email,
                        user.avatarUrl)
            }
        }
    }

    /**
      * Saves a user.
      *
      * @param user The user to save.
      * @return The saved user.
      */
    def save(user: User) = {
        val dbUser = DBUser(user.userId.toString, user.firstName, user.lastName, user.fullName, user.email, user.avatarUrl)
        val dbLoginInfo = DBLoginInfo(None, user.loginInfo.providerID, user.loginInfo.providerKey)
        // We don't have the LoginInfo id so we try to get it first.
        // If there is no LoginInfo yet for this user we retrieve the id on insertion.
        val loginInfoAction = {
            val retrieveLoginInfo = slickLoginInfos.filter(
                info => info.providerId === user.loginInfo.providerID &&
                    info.providerKey === user.loginInfo.providerKey).result.headOption
            val insertLoginInfo = slickLoginInfos.returning(slickLoginInfos.map(_.id)).
                into((info, id) => info.copy(id = Some(id))) += dbLoginInfo
            for {
                loginInfoOption <- retrieveLoginInfo
                loginInfo <- loginInfoOption.map(DBIO.successful(_)).getOrElse(insertLoginInfo)
            } yield loginInfo
        }
        // combine database actions to be run sequentially
        val actions = (for {
            _ <- slickUsers.insertOrUpdate(dbUser)
            loginInfo <- loginInfoAction
            _ <- slickUserLoginInfos += DBUserLoginInfo(dbUser.userId, loginInfo.id.get)
        } yield ()).transactionally
        // run actions and return user afterwards
        db.run(actions).map(_ => user)
    }

}



