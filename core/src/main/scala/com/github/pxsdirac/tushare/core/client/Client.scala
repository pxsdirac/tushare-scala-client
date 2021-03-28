package com.github.pxsdirac.tushare.core.client

import akka.actor.ActorSystem
import io.circe.{Decoder, Encoder}
import com.github.pxsdirac.tushare.core.{Fields, Token, Request => RequestTrait}

import scala.concurrent.{ExecutionContext, Future}

trait Client[Request, Response] {
  def request(req: Request): Future[Response]
}

object Client {
  implicit def rawHttpClient(implicit
      actorSystem: ActorSystem
  ): Client[RawHttpRequest, RawHttpResponse] = RawHttpClient.akkaHttpImpl

  implicit def genericClient[
      Request <: RequestTrait,
      Response
  ](implicit
      rawHttpClient: Client[RawHttpRequest, RawHttpResponse],
      token: Token,
      executionContext: ExecutionContext,
      requestEncoder: Encoder[Request],
      responseEncoder: Encoder[Response],
      responseDecoder: Decoder[Response],
      fields: Fields[Response]
  ): Client[Request, Seq[Response]] =
    req => {
      val rawHttpRequest = RawHttpRequest.from[Request, Response](req)
      rawHttpClient.request(rawHttpRequest).map(_.to[Response])
    }
}
