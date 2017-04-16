package service.user

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.db.User

/**
  * Created by pozpl on 11.04.17.
  */
trait DefaultEnv extends Env {

    /** Identity
      */
    type I = User

    /** Authenticator used for identification.
      *  [[com.mohiva.play.silhouette.impl.authenticators.BearerTokenAuthenticator]] could've also been used for REST.
      */
    type A = CookieAuthenticator//JWTAuthenticator

}