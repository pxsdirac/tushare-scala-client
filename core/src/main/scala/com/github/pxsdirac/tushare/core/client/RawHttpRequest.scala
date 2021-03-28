package com.github.pxsdirac.tushare.core.client

import com.github.pxsdirac.tushare.core.{Fields, Token, Request => RequestTrait}
import io.circe.{Encoder, Json}

case class RawHttpRequest(
    api_name: String,
    token: String,
    params: Json,
    fields: Seq[String]
)

object RawHttpRequest {
  def from[
      Request <: RequestTrait: Encoder,
      Response: Fields
  ](
      request: Request
  )(implicit token: Token): RawHttpRequest = {
    val apiName = request.apiName
    val fields = implicitly[Fields[Response]].fields
    val params = implicitly[Encoder[Request]].apply(request)
    RawHttpRequest(apiName, token.value, params, fields)
  }
}
