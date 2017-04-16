package controllers

import javax.inject.{Inject, Singleton}

import actors.ClientActor
import akka.actor._
import akka.stream.Materializer
import com.mohiva.play.silhouette.api.Silhouette
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import service.autorisation.AuthorizationServiceMemory
import service.conversations.ConversationsServiceImplInMemory
import service.routing.ChatEventBus
import service.session.ChatService
import service.user.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Application @Inject()(implicit val actorSystem: ActorSystem, eventBus: ChatEventBus,
                            implicit val mat: Materializer,
                            implicit val ec: ExecutionContext,
                            val messagesApi: MessagesApi,
                            val webJarAssets: WebJarAssets,
                            val silhouette: Silhouette[DefaultEnv]
                        ) extends Controller with I18nSupport {

    def index = silhouette.UserAwareAction.async { implicit request =>
        request.identity match {
            case Some(user) => Future.successful(Ok(views.html.home(user, webJarAssets)))
            case None => Future.successful(Redirect(routes.CredentialsAuthController.authenticate()))
        }
    }


    var authService = new AuthorizationServiceMemory();
    val conversationsService = new ConversationsServiceImplInMemory;
    val chatService = actorSystem.actorOf(Props(new ChatService(eventBus, authService, conversationsService)))


    def socket = WebSocket.accept[JsValue, JsValue] {
        (request: RequestHeader) => {
            ActorFlow.actorRef((out: ActorRef) => Props(new ClientActor(out, chatService, eventBus)))(actorSystem, mat)
        }
    }


}