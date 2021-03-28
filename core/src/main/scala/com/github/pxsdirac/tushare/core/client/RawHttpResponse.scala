package com.github.pxsdirac.tushare.core.client

import RawHttpResponse.{Data, ResponseInvalidException}
import io.circe.{Decoder, Json}

import scala.util.control.NoStackTrace

case class RawHttpResponse(
    code: Int,
    msg: Option[String],
    data: Option[Data]
) {
  def to[T: Decoder]: Seq[T] = {
    data match {
      case Some(d) =>
        d.items.flatMap { item =>
          val jObj = Json.obj(d.fields.zip(item.asArray.get): _*)

          implicitly[Decoder[T]].decodeJson(jObj) match {
            case Left(value) =>
              println(data)
              value.printStackTrace()

              throw value
            case Right(value) => Some(value)
          }
        }
      case None =>
        throw ResponseInvalidException(code, msg.getOrElse(""))
    }
  }

}

object RawHttpResponse {
  case class Data(fields: Seq[String], items: Seq[Json])
  case class ResponseInvalidException(code: Int, msg: String)
      extends NoStackTrace {
    override def getMessage: String =
      s"response is invalid: code=$code, msg=$msg"
  }
}
