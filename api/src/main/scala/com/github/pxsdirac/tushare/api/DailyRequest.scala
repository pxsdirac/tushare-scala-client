package com.github.pxsdirac.tushare.api

import com.github.pxsdirac.tushare.core.Request

case class DailyRequest(
    ts_code: String,
    trade_date: Option[String] = None,
    start_date: Option[String] = None,
    end_date: Option[String] = None
) extends Request {
  override val apiName: String = "daily"
  override type Response = DailyResponse
}
