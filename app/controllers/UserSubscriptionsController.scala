package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import models.db.{User, UserIndividualSubscription}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.Controller
import service.subscriptions.IndividualSubscriptionsService
import service.user.DefaultEnv

import scala.concurrent.Future

/**
  * Created by pozpl on 23.04.17.
  */
class UserSubscriptionsController @Inject()(
                                               val messagesApi: MessagesApi,
                                               val env: Environment[DefaultEnv],
                                               socialProviderRegistry: SocialProviderRegistry,
                                               val silhouette: Silhouette[DefaultEnv],
                                               val individualSubscriptionsService: IndividualSubscriptionsService
                                           ) extends Controller with I18nSupport{

    import play.api.libs.concurrent.Execution.Implicits._

    def list = silhouette.UserAwareAction.async {  implicit request =>

        implicit val userWriter = Json.writes[User]

        request.identity match {
            case Some(owner) => individualSubscriptionsService.getIndividualSubscriptions(owner).map(userList => Ok(Json.toJson(userList)))
            case None => Future.successful(Redirect(routes.CredentialsAuthController.authenticate()))
        }
    }

    def subscribe = silhouette.UserAwareAction.async(parse.json) {  implicit request =>

        implicit val userWriter = Json.writes[User]
        implicit val userReader = Json.reads[User]
        implicit val subscriptionWriter = Json.writes[UserIndividualSubscription]

        request.identity match {
            case Some(owner) => {
                val userToSubscribe = request.body.as[User]
                individualSubscriptionsService.subscribeToUser(owner, userToSubscribe)
                    .map(userIndividualSubscription => Ok(Json.toJson(userIndividualSubscription)))
            }
            case None => Future.successful(Redirect(routes.CredentialsAuthController.authenticate()))
        }
    }

    def unsubscribe(userUidToUnsubscribe:String) = silhouette.SecuredAction.async{ implicit request =>
         individualSubscriptionsService.deleteSubscription(request.identity, userUidToUnsubscribe).map(isOk => Ok(Json.toJson(isOk)))
    }

}
