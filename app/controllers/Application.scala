package controllers

import javax.inject.{Inject, Singleton}

import actors.ClientActor
import akka.actor._
import akka.stream.Materializer
import com.mohiva.play.silhouette.api.{HandlerResult, Silhouette}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import service.conversations.ConversationsServiceImplInMemory
import service.routing.ChatEventBus
import service.session.ChatService
import service.subscriptions.IndividualSubscriptionsService
import service.user.{DefaultEnv, UserService}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Application @Inject()(implicit val actorSystem: ActorSystem, eventBus: ChatEventBus,
                            implicit val mat: Materializer,
                            implicit val ec: ExecutionContext,
                            val messagesApi: MessagesApi,
                            val webJarAssets: WebJarAssets,
                            val silhouette: Silhouette[DefaultEnv] ,
                            val userService: UserService,
                            val individualSubscriptionsService: IndividualSubscriptionsService
                        ) extends Controller with I18nSupport {

    def index = silhouette.UserAwareAction.async { implicit request =>
        request.identity match {
            case Some(user) => Future.successful(Ok(views.html.home(user, webJarAssets)))
            case None => Future.successful(Redirect(routes.CredentialsAuthController.authenticate()))
        }
    }


    val conversationsService = new ConversationsServiceImplInMemory;
    val chatService = actorSystem.actorOf(Props(new ChatService(eventBus, conversationsService, userService, individualSubscriptionsService)))


    def socket = WebSocket.acceptOrResult[JsValue, JsValue] { implicit request =>
        implicit val req = Request(request, AnyContentAsEmpty)
        silhouette.SecuredRequestHandler { securedRequest =>
            Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
        }.map {
            case HandlerResult(r, None) => Left(Forbidden)
            case HandlerResult(r, Some(user)) => Right(ActorFlow.actorRef((out: ActorRef) =>  ClientActor.props(user, out, chatService)))
        }
    }

//    def socket = WebSocket.tryAcceptWithActor[String, String] { request =>
//        implicit val req = Request(request, AnyContentAsEmpty)
//        silhouette.SecuredRequestHandler { securedRequest =>
//            Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
//        }.map {
//            case HandlerResult(r, Some(user)) => Right(MyWebSocketActor.props(user) _)
//            case HandlerResult(r, None) => Left(r)
//        }
//    }

}