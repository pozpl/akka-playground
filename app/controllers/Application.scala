package controllers

import javax.inject.{Inject, Singleton}

import actors.ClientActor
import akka.actor._
import play.api.Play.current
import play.api.libs.json.JsValue
import play.api.mvc._
import service.autorisation.AuthorizationServiceMemory
import service.conversations.ConversationsServiceImplInMemory
import service.routing.ChatEventBus
import service.session.ChatService

@Singleton
class Application @Inject()(actorSystem: ActorSystem, eventBus: ChatEventBus)  extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  // creates actor of type chat described above
//  val chat = actorSystem.actorOf(Props[Chat], "chat")
//  val eventBus = new ChatEventBus
  var authService = new AuthorizationServiceMemory();
  val conversationsService = new ConversationsServiceImplInMemory;
  val chatService = actorSystem.actorOf(Props(new ChatService(eventBus, authService, conversationsService)))
  /*
   Specifies how to wrap an out-actor that will represent
   WebSocket connection for a given request.
  */
  def socket = WebSocket.acceptWithActor[JsValue, JsValue] {
    (request: RequestHeader) => {
      (out: ActorRef) => Props(new ClientActor(out, chatService, eventBus))
    }
  }


}