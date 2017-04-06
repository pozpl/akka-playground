package controllers

import javax.inject.{Inject, Singleton}

import actors.ClientActor
import akka.actor._
import akka.stream.Materializer
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import service.autorisation.AuthorizationServiceMemory
import service.conversations.ConversationsServiceImplInMemory
import service.routing.ChatEventBus
import service.session.ChatService

import scala.concurrent.ExecutionContext

@Singleton
class Application @Inject()(actorSystem: ActorSystem, eventBus: ChatEventBus,
                            implicit val mat: Materializer,
                            implicit val ec: ExecutionContext)  extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }


  var authService = new AuthorizationServiceMemory();
  val conversationsService = new ConversationsServiceImplInMemory;
  val chatService = actorSystem.actorOf(Props(new ChatService(eventBus, authService, conversationsService)))

  
  def socket = WebSocket.accept[JsValue, JsValue] {
    (request: RequestHeader) => {
      ActorFlow.actorRef((out:ActorRef )=> Props(new ClientActor(out, chatService, eventBus)))
    }
  }


}