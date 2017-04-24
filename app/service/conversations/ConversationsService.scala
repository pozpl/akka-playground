package service.conversations

import service.protocol.ReceivedTextMessage

import scala.concurrent.Future

/**
  * Created by pozpl on 21.03.17.
  */
trait TConversationsService {

     def registerTextMessage(receivedTextMessage: ReceivedTextMessage):Unit
     def getTextMessagesForPrivateChat(pearOne:String, pearTwo:String):Future[List[ReceivedTextMessage]]
}

/**
  * This thing should work with Software Transactional Memory eventually
  * And I need to pass context for future from play
  */
class ConversationsServiceImplInMemory() extends TConversationsService{

    override def registerTextMessage(receivedTextMessage: ReceivedTextMessage): Unit = {
        ConversationsServiceImplInMemory.conversation =  receivedTextMessage  :: ConversationsServiceImplInMemory.conversation
    }

    import scala.concurrent.ExecutionContext.Implicits.global

    override def getTextMessagesForPrivateChat(pearOne: String, pearTwo: String): Future[List[ReceivedTextMessage]] = {
        val oneList = ConversationsServiceImplInMemory.conversation.filter((message:ReceivedTextMessage) => {
            message.sender.userId.toString == pearOne
        })
        val twoList = ConversationsServiceImplInMemory.conversation.filter((message:ReceivedTextMessage) => {
            message.sender.userId.toString == pearTwo
        })

        val fullList = (oneList ::: twoList).sortWith((x, y) => x.timeStump < y.timeStump)
        Future(fullList)
    }
}

object ConversationsServiceImplInMemory{
    var conversation: List[ReceivedTextMessage] = List()

}