package dao.repository

import com.google.inject.Inject
import dao.schema.UsersTable
import models.db.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Created by pozpl on 03.04.17.
  */
class UsersDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[JdbcProfile] with UsersTable{

    import dbConfig.profile.api._
    
    def getByUid(uid:String):Future[Option[User]] = {
        db.run((for (user <- users if user.uid === uid) yield user).result.headOption)
    }
    
    def getById(id:Long):Future[Option[User]] = {
        db.run((for (user <- users if user.id === id) yield user).result.headOption)
    }

    def insert(user: User) = db.run(users returning users.map(_.id) += user)
        .map(id => user.copy(id = Some(id)))

    
}
