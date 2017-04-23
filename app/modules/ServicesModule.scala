package modules

import com.google.inject.AbstractModule
import dao.repository.{UserSubscriptionsDao, UserSubscriptionsDaoImpl}
import net.codingwell.scalaguice.ScalaModule
import play.api.libs.concurrent.AkkaGuiceSupport
import service.subscriptions.{IndividualSubscriptionsService, IndividualSubscriptionsServiceImpl}

/**
  * Created by pozpl on 23.04.17.
  */
class ServicesModule extends AbstractModule with ScalaModule with AkkaGuiceSupport{
    override def configure(): Unit = {
        bind[UserSubscriptionsDao].to[UserSubscriptionsDaoImpl]
        bind[IndividualSubscriptionsService].to[IndividualSubscriptionsServiceImpl]
    }
}
