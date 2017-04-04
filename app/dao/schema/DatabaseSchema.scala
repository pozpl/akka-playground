package dao.schema

import java.util.Date

import models.db.{Message, User}
import slick.jdbc.MySQLProfile.api._



/**
  * Created by pozpl on 03.04.17.
  */
trait UsersTable{
    protected class Users(tag: Tag) extends Table[User](tag, "users") {

        def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
        def uid = column[String]("uid")
        def login = column[String]("login")
        def mobile = column[Option[Long]]("mobile")
        def email = column[Option[String]]("email")

        override def * =
            (id.?, uid, login, mobile, email) <>(User.tupled, User.unapply)
    }

    protected val users = TableQuery[Users]
}

trait MessagesTable{
    protected class Messages(tag: Tag) extends Table[Message](tag, "messages") {
        def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
        def from = column[String]("from")
        def toSector = column[String]("to_sector")
        def toTarget = column[Option[String]]("to_target")
        def dateTime = column[Date]("date")

        implicit  val utilDate2SqlTimestampMapper = MappedColumnType.base[java.util.Date, java.sql.Timestamp](
            utilDate => new java.sql.Timestamp(utilDate.getTime()) ,
            sqlTimestamp => new java.util.Date(sqlTimestamp.getTime())
        )

        override def * =
            (id.?, from, toSector, toTarget, dateTime) <> (Message.tupled, Message.unapply)
    }

    protected val messages = TableQuery[Messages]
}
