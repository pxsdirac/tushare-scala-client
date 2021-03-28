package com.github.pxsdirac.tushare.api

import com.github.pxsdirac.tushare.core.Request

case class MarginRequest(
    trade_date: Option[String] = None,
    exchange_id: Option[String] = None,
    start_date: Option[String] = None,
    end_date: Option[String] = None
) extends Request {
  override val apiName: String = "margin"
  override type Response = MarginResponse
}
