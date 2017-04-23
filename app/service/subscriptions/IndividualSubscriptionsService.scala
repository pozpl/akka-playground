package service.subscriptions

import javax.inject.Inject

import dao.repository.UserSubscriptionsDao
import models.db.{User, UserIndividualSubscription}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

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

}


class IndividualSubscriptionsServiceImpl @Inject() (userSubscriptionsDao: UserSubscriptionsDao) extends IndividualSubscriptionsService{

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
                val userIndividualSubscription = new UserIndividualSubscription(None, user.userId.toString, userToSubscribe.userId.toString, 0)
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
}