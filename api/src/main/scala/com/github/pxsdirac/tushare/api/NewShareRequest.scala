package com.github.pxsdirac.tushare.api

import com.github.pxsdirac.tushare.core.Request

case class NewShareRequest(
    start_date: Option[String],
    end_date: Option[String]
) extends Request {
  override val apiName: String = "new_share"
  override type Response = NewShareResponse
}
