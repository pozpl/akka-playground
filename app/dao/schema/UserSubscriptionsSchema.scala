package dao.schema

import models.db.UserIndividualSubscription


/**
  * Created by pozpl on 21.04.17.
  */
trait UserSubscriptionsSchema extends DbTableDefinitions{
    
    import driver.api._

    protected class UserIndividualSubscriptions(tag: Tag) extends Table[UserIndividualSubscription](tag, "user_individual_subscription"){
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def userUid = column[String]("user_id")
        def userFk = foreignKey("uis_user_id_fk", userUid, slickUsers)(_.id, ForeignKeyAction.Restrict, ForeignKeyAction.NoAction)
        def subscribedUserUid = column[String]("subscribed_user_id")
        def subscribedUserFk = foreignKey("uis_subscribed_user_id_fk", subscribedUserUid, slickUsers)(_.id, ForeignKeyAction.Restrict)

        def position = column[Int]("position")

        def * = (id.?, userUid, subscribedUserUid, position) <> (UserIndividualSubscription.tupled, UserIndividualSubscription.unapply)
    }

    val userSubscriptions = TableQuery[UserIndividualSubscriptions]
}

