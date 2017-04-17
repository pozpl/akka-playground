package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{Environment, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller
import service.user.DefaultEnv

import scala.concurrent.Future

/**
  * The basic application controller.
  *
  * @param messagesApi            The Play messages API.
  * @param env                    The Silhouette environment.
  * @param socialProviderRegistry The social provider registry.
  */
class ApplicationController @Inject()(
                                         val messagesApi: MessagesApi,
                                         val env: Environment[DefaultEnv],
                                         socialProviderRegistry: SocialProviderRegistry,
                                         val silhouette: Silhouette[DefaultEnv],
                                         webJarAssets: WebJarAssets
                                     )
    extends Controller with I18nSupport {

    /**
      * Handles the index action.
      *
      * @return The result to display.
      */
    def index = silhouette.UserAwareAction.async { implicit request =>
//        Future.successful(Ok(views.html.home(request.identity)))
        request.identity match {
            case Some(user) => Future.successful(Ok(views.html.index(Some(user), webJarAssets)))
            case None => Future.successful(Redirect(routes.CredentialsAuthController.authenticate()))
        }
    }

    /**
      * Handles the Sign In action.
      *
      * @return The result to display.
      */
    def signIn = silhouette.UserAwareAction.async { implicit request =>
        request.identity match {
            case Some(user) => Future.successful(Redirect(routes.ApplicationController.index()))
            case None => Future.successful(Ok(views.html.signIn(SignInForm.form, socialProviderRegistry, webJarAssets)))
        }
    }

    /**
      * Handles the Sign Up action.
      *
      * @return The result to display.
      */
    def signUp = silhouette.UserAwareAction.async { implicit request =>
        request.identity match {
            case Some(user) => Future.successful(Redirect(routes.ApplicationController.index()))
            case None => Future.successful(Ok(views.html.signUp(SignUpForm.form, webJarAssets)))
        }
    }

    /**
      * Handles the Sign Out action.
      *
      * @return The result to display.
      */
    def signOut = silhouette.SecuredAction.async { implicit request =>
        val result = Redirect(routes.ApplicationController.index())
        env.eventBus.publish(LogoutEvent(request.identity, request))

        env.authenticatorService.discard(request.authenticator, result)
    }
}
