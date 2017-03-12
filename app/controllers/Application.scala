package controllers

import javax.inject.{Inject, Singleton}

import actors.ClientActor
import akka.actor._
import play.api.Play.current
import play.api.libs.json.JsValue
import play.api.mvc._
import service.routing.ChatEventBus
import service.session.ChatService

@Singleton
class Application @Inject()(actorSystem: ActorSystem)  extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  // creates actor of type chat described above
//  val chat = actorSystem.actorOf(Props[Chat], "chat")
  val eventBus = new ChatEventBus
  val chatService = actorSystem.actorOf(Props(new ChatService(eventBus)))
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