package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import models.db.User
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.Controller
import service.user.{DefaultEnv, UserService}

import scala.concurrent.Future

/**
  * Created by pozpl on 18.04.17.
  */
class UsersListController @Inject()(
                             val messagesApi: MessagesApi,
                             val env: Environment[DefaultEnv],
                             socialProviderRegistry: SocialProviderRegistry,
                             val silhouette: Silhouette[DefaultEnv],
                             val usersService: UserService
                         ) extends Controller with I18nSupport{

    import play.api.libs.concurrent.Execution.Implicits._
    
    def list = silhouette.UserAwareAction.async {  implicit request =>

        implicit val userWriter = Json.writes[User]

        request.identity match {
            case Some(_) => usersService.list().map(userList => Ok(Json.toJson(userList)))
            case None => Future.successful(Redirect(routes.CredentialsAuthController.authenticate()))
        }
    }

}
