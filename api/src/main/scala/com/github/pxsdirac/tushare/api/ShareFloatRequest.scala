package com.github.pxsdirac.tushare.api

import com.github.pxsdirac.tushare.core.Request

case class ShareFloatRequest(
    ts_code: String,
    ann_date: Option[String] = None,
    float_date: Option[String] = None,
    start_date: Option[String] = None,
    end_date: Option[String] = None
) extends Request {
  override val apiName: String = "share_float"
  override type Response = ShareFloatResponse
}
