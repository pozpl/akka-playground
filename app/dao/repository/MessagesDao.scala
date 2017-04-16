package dao.repository

import com.google.inject.Inject
import dao.schema.DbTableDefinitions
import models.db.Message
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Created by pozpl on 04.04.17.
  */
class MessagesDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
    extends DaoSlick with DbTableDefinitions {

    import driver.api._
    

    def getById(id: Long): Future[Option[Message]] = {
        db.run((for (message <- messages if message.id === id) yield message).result.headOption)
    }

    def insert(message: Message) = db.run(messages returning messages.map(_.id) += message)
        .map(id => message.copy(id = Some(id)))
}
