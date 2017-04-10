package dao.repository

import dao.schema.DbTableDefinitions
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile

/**
  * 
  * Created by pozpl on 10.04.17.
  */
trait DaoSlick extends DbTableDefinitions with  HasDatabaseConfigProvider[JdbcProfile]{

}
