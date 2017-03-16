package service.autorisation


import scala.concurrent.Future

/**
  * Created by pozpl on 12.03.17.
  */
trait AuthorizationService {

    def isUserExists(uid : String):Future[Boolean]

}

class AuthorizationServiceMemory() extends AuthorizationService{

    import scala.concurrent.ExecutionContext.Implicits.global

    override def isUserExists(uid: String): Future[Boolean] = {
        Future(true)
    }
}
