package dao.schema

import java.util.Date

import com.mohiva.play.silhouette.api.LoginInfo
import models.db.Message
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape.proveShapeOf


trait DbTableDefinitions {

    protected val driver: JdbcProfile

    import driver.api._

    case class DBUser(
                         userId: String,
                         firstName: Option[String],
                         lastName: Option[String],
                         fullName: Option[String],
                         email: Option[String],
                         avatarUrl: Option[String]
                     )

    class Users(tag: Tag) extends Table[DBUser](tag, "user") {
        def id = column[String]("user_id", O.PrimaryKey)

        def firstName = column[Option[String]]("first_name")

        def lastName = column[Option[String]]("last_name")

        def fullName = column[Option[String]]("full_name")

        def email = column[Option[String]]("email")

        def avatarUrl = column[Option[String]]("avatar_url")

        def * = (id, firstName, lastName, fullName, email, avatarUrl) <> (DBUser.tupled, DBUser.unapply)
    }

    case class DBLoginInfo(id: Option[Long], providerId: String, providerKey: String)

    class LoginInfos(tag: Tag) extends Table[DBLoginInfo](tag, "login_info") {
        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

        def providerId = column[String]("provider_id")

        def providerKey = column[String]("provider_key")

        def * = (id.?, providerId, providerKey) <> (DBLoginInfo.tupled, DBLoginInfo.unapply)
    }

    case class DBUserLoginInfo(userId: String, loginInfoId: Long)

    class UserLoginInfos(tag: Tag) extends Table[DBUserLoginInfo](tag, "user_login_info") {
        def userId = column[String]("user_id")

        def loginInfoId = column[Long]("login_info_id")

        def * = (userId, loginInfoId) <> (DBUserLoginInfo.tupled, DBUserLoginInfo.unapply)
    }

    case class DBPasswordInfo(
                                 hasher: String,
                                 password: String,
                                 salt: Option[String],
                                 loginInfoId: Long
                             )

    class PasswordInfos(tag: Tag) extends Table[DBPasswordInfo](tag, "password_info") {
        def hasher = column[String]("hasher")

        def password = column[String]("password")

        def salt = column[Option[String]]("salt")

        def loginInfoId = column[Long]("login_info_id")

        def * = (hasher, password, salt, loginInfoId) <> (DBPasswordInfo.tupled, DBPasswordInfo.unapply)
    }

    case class DBOAuth1Info(
                               id: Option[Long],
                               token: String,
                               secret: String,
                               loginInfoId: Long
                           )

    class OAuth1Infos(tag: Tag) extends Table[DBOAuth1Info](tag, "oauth1_info") {
        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

        def token = column[String]("token")

        def secret = column[String]("secret")

        def loginInfoId = column[Long]("login_info_id")

        def * = (id.?, token, secret, loginInfoId) <> (DBOAuth1Info.tupled, DBOAuth1Info.unapply)
    }

    case class DBOAuth2Info(
                               id: Option[Long],
                               accessToken: String,
                               tokenType: Option[String],
                               expiresIn: Option[Int],
                               refreshToken: Option[String],
                               loginInfoId: Long
                           )

    class OAuth2Infos(tag: Tag) extends Table[DBOAuth2Info](tag, "oauth2_info") {
        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

        def accessToken = column[String]("accesstoken")

        def tokenType = column[Option[String]]("tokentype")

        def expiresIn = column[Option[Int]]("expiresin")

        def refreshToken = column[Option[String]]("refreshtoken")

        def loginInfoId = column[Long]("login_info_id")

        def * = (id.?, accessToken, tokenType, expiresIn, refreshToken, loginInfoId) <> (DBOAuth2Info.tupled, DBOAuth2Info.unapply)
    }

    case class DBOpenIDInfo(
                               id: String,
                               loginInfoId: Long
                           )

    class OpenIDInfos(tag: Tag) extends Table[DBOpenIDInfo](tag, "openid_info") {
        def id = column[String]("id", O.PrimaryKey)

        def loginInfoId = column[Long]("login_info_id")

        def * = (id, loginInfoId) <> (DBOpenIDInfo.tupled, DBOpenIDInfo.unapply)
    }

    case class DBOpenIDAttribute(
                                    id: String,
                                    key: String,
                                    value: String
                                )

    class OpenIDAttributes(tag: Tag) extends Table[DBOpenIDAttribute](tag, "openid_attributes") {
        def id = column[String]("id")

        def key = column[String]("key")

        def value = column[String]("value")

        def * = (id, key, value) <> (DBOpenIDAttribute.tupled, DBOpenIDAttribute.unapply)
    }

    // table query definitions
    val slickUsers = TableQuery[Users]
    val slickLoginInfos = TableQuery[LoginInfos]
    val slickUserLoginInfos = TableQuery[UserLoginInfos]
    val slickPasswordInfos = TableQuery[PasswordInfos]
    val slickOAuth1Infos = TableQuery[OAuth1Infos]
    val slickOAuth2Infos = TableQuery[OAuth2Infos]
    val slickOpenIDInfos = TableQuery[OpenIDInfos]
    val slickOpenIDAttributes = TableQuery[OpenIDAttributes]

    // queries used in multiple places
    def loginInfoQuery(loginInfo: LoginInfo) =
        slickLoginInfos.filter(dbLoginInfo => dbLoginInfo.providerId === loginInfo.providerID && dbLoginInfo.providerKey === loginInfo.providerKey)


    class Messages(tag: Tag) extends Table[Message](tag, "messages") {
        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

        def from = column[String]("from_uid")

        def toSegment = column[String]("to_segment")

        def toTarget = column[Option[String]]("to_target")

        def dateTime = column[Date]("date")(utilDate2SqlTimestampMapper)

        def text = column[String]("text")

        implicit val utilDate2SqlTimestampMapper = MappedColumnType.base[java.util.Date, java.sql.Timestamp](
            utilDate => new java.sql.Timestamp(utilDate.getTime()),
            sqlTimestamp => new java.util.Date(sqlTimestamp.getTime())
        )

        override def * =
            (id.?, from, toSegment, toTarget, dateTime, text) <> (Message.tupled, Message.unapply)
    }


    val messages = TableQuery[Messages]
}




