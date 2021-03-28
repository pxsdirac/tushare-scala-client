package com.github.pxsdirac.tushare.core.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.Json

import scala.concurrent.Future
import scala.util.Failure

trait RawHttpClient extends Client[RawHttpRequest, RawHttpResponse] {
  override def request(req: RawHttpRequest): Future[RawHttpResponse]
}

object RawHttpClient {
  import io.circe.generic.auto._
  import io.circe.syntax._
  def akkaHttpImpl(implicit actorSystem: ActorSystem): RawHttpClient =
    (req: RawHttpRequest) => {
      import actorSystem.dispatcher
      val entity = req.asJson.noSpaces
      Http()
        .singleRequest(
          HttpRequest(
            uri = "http://api.waditu.com",
            method = HttpMethods.POST,
            entity = HttpEntity(ContentTypes.`application/json`, entity)
          )
        )
        .flatMap { response =>
          Unmarshal(response).to[RawHttpResponse].andThen {
            case Failure(exception) =>
              Unmarshal(response).to[Json].foreach(println)
              exception.printStackTrace()
          }
        }
    }
}
