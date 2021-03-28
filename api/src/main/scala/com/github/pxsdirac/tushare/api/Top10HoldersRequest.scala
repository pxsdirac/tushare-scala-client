package com.github.pxsdirac.tushare.api

import com.github.pxsdirac.tushare.core.Request

case class Top10HoldersRequest(
    ts_code: String,
    period: Option[String] = None,
    ann_date: Option[String] = None,
    start_date: Option[String] = None,
    end_date: Option[String] = None
) extends Request {
  override val apiName: String = "top10_holders"
  override type Response = Top10HoldersResponse
}
