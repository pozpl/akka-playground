package controllers

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AvatarService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers._
import forms.SignUpForm
import models.db.User
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Action, Controller}
import service.user.{DefaultEnv, UserService}

import scala.concurrent.Future

/**
  * The sign up controller.
  *
  * @param messagesApi The Play messages API.
  * @param env The Silhouette environment.
  * @param userService The user service implementation.
  * @param authInfoRepository The auth info repository implementation.
  * @param avatarService The avatar service implementation.
  * @param passwordHasher The password hasher implementation.
  */
class SignUpController @Inject() (
                                     val messagesApi: MessagesApi,
                                     val env: Environment[DefaultEnv],
                                     userService: UserService,
                                     authInfoRepository: AuthInfoRepository,
                                     avatarService: AvatarService,
                                     passwordHasher: PasswordHasher,
                                     webJarsAssets: WebJarAssets
                                 )
    extends Controller with I18nSupport{

    /**
      * Registers a new user.
      *
      * @return The result to display.
      */
    def signUp = Action.async { implicit request =>
        SignUpForm.form.bindFromRequest.fold(
            form => Future.successful(BadRequest(views.html.signUp(form, webJarsAssets))),
            data => {
                val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)
                userService.retrieve(loginInfo).flatMap {
                    case Some(user) =>
                        Future.successful(Redirect(routes.ApplicationController.signUp()).flashing("error" -> Messages("user.exists")))
                    case None =>
                        val authInfo = passwordHasher.hash(data.password)
                        val user = User(
                            userId = UUID.randomUUID(),
                            loginInfo = loginInfo,
                            firstName = Some(data.firstName),
                            lastName = Some(data.lastName),
                            fullName = Some(data.firstName + " " + data.lastName),
                            email = Some(data.email),
                            avatarUrl = None
                        )
                        for {
                            avatar <- avatarService.retrieveURL(data.email)
                            user <- userService.save(user.copy(avatarUrl = avatar))
                            authInfo <- authInfoRepository.add(loginInfo, authInfo)
                            authenticator <- env.authenticatorService.create(loginInfo)
                            value <- env.authenticatorService.init(authenticator)
                            result <- env.authenticatorService.embed(value, Redirect(routes.ApplicationController.index()))
                        } yield {
                            env.eventBus.publish(SignUpEvent(user, request))
                            env.eventBus.publish(LoginEvent(user, request))
                            result
                        }
                }
            }
        )
    }
}
