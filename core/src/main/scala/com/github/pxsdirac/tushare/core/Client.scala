package com.github.pxsdirac.tushare.core

import akka.actor.ActorSystem
import com.github.pxsdirac.tushare.core.cache.Cache
import com.github.pxsdirac.tushare.core.{Request => RequestTrait}
import io.circe.{Decoder, Encoder}

import scala.concurrent.{Future, Promise}
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.Success

object Client {
  case class Delay(finiteDuration: FiniteDuration)

  def request[Request <: RequestTrait](
      request: Request
  )(implicit
      requestEncoder: Encoder[Request],
      responseEncoder: Encoder[request.Response],
      responseDecoder: Decoder[request.Response],
      client: com.github.pxsdirac.tushare.core.client.Client[Request, Seq[
        request.Response
      ]],
      cache: Cache,
      actorSystem: ActorSystem,
      delay: Delay = Delay(0.seconds)
  ): Future[Seq[request.Response]] = {
    import io.circe.syntax._
    import io.circe.parser._
    import actorSystem.dispatcher
    val requestJson = request.asJson.noSpaces
    val key = request.getClass.getSimpleName + ":" + requestJson
    cache.get(key).flatMap {
      case Some(value) =>
        Future.fromTry(decode[Seq[request.Response]](value).toTry)
      case None =>
        val promise: Promise[Unit] = Promise[Unit]
        actorSystem.scheduler.scheduleOnce(delay.finiteDuration) {
          promise.success(())
        }
        promise.future
          .flatMap { _ =>
            client.request(request)
          }
          .andThen {
            case Success(response) =>
              cache.save(key, response.asJson.noSpaces)
          }
    }
  }
}
