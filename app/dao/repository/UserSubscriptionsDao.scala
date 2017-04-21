package dao.repository

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import dao.schema.UserSubscriptionsSchema
import models.db.{User, UserIndividualSubscription}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

/**
  * Created by pozpl on 21.04.17.
  */
trait UserSubscriptionsDao {

    def find(id: Int): Future[Option[UserIndividualSubscription]]

    /**
      * Get subscriptions of users
      *
      * @param user
      * @return
      */
    def find(user: User): Future[List[User]]

    /**
      * Save subscription
      *
      * @param userSubscription
      * @return
      */
    def save(userSubscription: UserIndividualSubscription): Future[UserIndividualSubscription]
}

class UserSubscriptionsDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
    extends UserSubscriptionsDao with UserSubscriptionsSchema with DaoSlick {

    import driver.api._


    /**
      * Get subscriptions of users
      *
      * @param user
      * @return
      */
    override def find(user: User): Future[Seq[User]] = {
        val query = for {
            (subsciption, subscriptionUser) <- userSubscriptions join slickUsers if subscriptionUser.id === subsciption.subscribedUserUid
            dbUserLoginInfo <- slickUserLoginInfos.filter(_.userId === subscriptionUser.id)
            dbLoginInfo <- slickLoginInfos.filter(_.id === dbUserLoginInfo.loginInfoId)
            if subsciption.userUid === user.userId.toString
        } yield (subscriptionUser, dbLoginInfo)
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
      * Save subscription
      *
      * @param userSubscription
      * @return
      */
    override def save(userSubscription: UserIndividualSubscription): Future[UserIndividualSubscription] = {
        db.run(userSubscriptions returning userSubscriptions.map(_.id) += userSubscription)
            .map(id => userSubscription.copy(id = Some(id)))
    }

    override def find(id: Int): Future[Option[UserIndividualSubscription]] = {
        
        val query = for {
            dbSubscription <- userSubscriptions
            if dbSubscription.id === id
        } yield dbSubscription
        db.run(query.result.headOption)
    }
}