package service.subscriptions

import javax.inject.Inject

import dao.repository.UserSubscriptionsDao
import models.db.{User, UserIndividualSubscription}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import service.user.UserService

/**
  * Created by pozpl on 21.04.17.
  */
trait IndividualSubscriptionsService {

    /**
      * Subscribe one user to another
      * @param user
      * @param userToSubscribe
      * @return
      */
    def subscribeToUser(user: User, userToSubscribe: User):Future[UserIndividualSubscription]

    /**
      * Get users to whom user subscribed
      * @param user
      * @return
      */
    def getIndividualSubscriptions(user: User): Future[Seq[User]]

    /**
      * Is user subscribed to another user
      * @param user
      * @param userToSubscribe
      * @return
      */
    def isSubscribed(user: User, userToSubscribe:User):Future[Boolean]

    /**
      * Delete subscriptions of user to another user
      * @param user
      * @param userToSubscribe
      * @return
      */
    def deleteSubscription(user:User, userToSubscribe:User):Future[Boolean]

    def deleteSubscription(subscriptionOwner:User, subscribedToUid: String):Future[Boolean]

}


class IndividualSubscriptionsServiceImpl @Inject() (   usersService: UserService,
                                                       userSubscriptionsDao: UserSubscriptionsDao) extends IndividualSubscriptionsService{

    /**
      * Subscribe one user to another
      *
      * @param user
      * @param userToSubscribe
      * @return
      */
    override def subscribeToUser(user: User, userToSubscribe: User): Future[UserIndividualSubscription] = {
        val foundSubscription: Future[Option[UserIndividualSubscription]] = userSubscriptionsDao.find(user, userToSubscribe)
        foundSubscription.flatMap((subOpt:Option[UserIndividualSubscription]) => subOpt match {
            case Some(subscription) => Future.successful(subscription)
            case None => {
                val userIndividualSubscription = UserIndividualSubscription(None, user.userId.toString, userToSubscribe.userId.toString, 0)
                userSubscriptionsDao.save(userIndividualSubscription)
            }
        })
    }

    /**
      * Get users to whom user subscribed
      *
      * @param user
      * @return
      */
    override def getIndividualSubscriptions(user: User): Future[Seq[User]] = {
        userSubscriptionsDao.find(user)
    }

    /**
      * Is user subscribed to another user
      *
      * @param user
      * @param userToSubscribe
      * @return
      */
    override def isSubscribed(user: User, userToSubscribe: User): Future[Boolean] = {
        userSubscriptionsDao.find(user, userToSubscribe).map(subscriptionOpt => {
            subscriptionOpt match {
                case Some(_) => true
                case None => false
            }
        })
    }

    /**
      * Delete subscriptions of user to another user
      *
      * @param user
      * @param userToSubscribe
      * @return
      */
    override def deleteSubscription(user: User, userToSubscribe: User): Future[Boolean] = {
        userSubscriptionsDao.delete(user, userToSubscribe).map(_ > 0)
    }

    override def deleteSubscription(subscriptionOwner: User, subscribedToUid: String): Future[Boolean] = {
        usersService.find(subscribedToUid).flatMap((userOpt:Option[User]) =>{
            userOpt match {
                case  Some(user) => deleteSubscription(subscriptionOwner, user)
                case _ => Future.successful(false)
            }
        })
    }
}