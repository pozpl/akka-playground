package service.routing

import akka.event.{ActorEventBus, SubchannelClassification}
import akka.util.Subclassification
import service.protocol.ReceivedTextMessage

/**
  * Created by pozpl on 28.02.17.
  */
object ChatSegments {
    val Individual = "individual"
    val Sector = "sectors"
    val Group = "groups"
}


case class ChatCoordinate(segment: String, target: Option[String])

//sealed trait Chat
//case class ChatMessage(source: String, message: String) extends Chat
//case class ChatEvent(coord:ChatCoordinate, msg: ChatMessage) extends Chat

class ChatEventBus extends ActorEventBus with SubchannelClassification {
    type Event = ReceivedTextMessage
    type Classifier = ChatCoordinate

    protected def classify(event: Event): Classifier = event.textMessage.to

    protected def subclassification = new Subclassification[Classifier] {
        def isEqual(x: Classifier, y: Classifier) = (x.segment == y.segment && x.target == y.target)

        /* Is X a subclass of Y? e.g. is Player/Bob a subclass of Player/None? YES. Is Player/None a subclass of Player/Bob? NO. */
        def isSubclass(x: Classifier, y: Classifier) = {
            val res = (x.segment == y.segment && x.target == None)
            //println(s"Subclass check: $x $y: $res")
            res
        }
    }

    protected def publish(event: Event, subscriber: Subscriber): Unit = {
        subscriber ! event
    }
}

//object ChatEvent{
//    implicit def jsValue2ChatEvent(jsValue : JsValue): ChatEvent = {
//        jsValue.as[ChatEvent]
//    }
//
//    implicit def ChatEvent2JsValue(chatEvent: ChatEvent): JsValue = {
//        Json.toJson(chatEvent)
//    }
//}