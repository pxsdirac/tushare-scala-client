package com.github.pxsdirac.tushare.api

import com.github.pxsdirac.tushare.core.Request

case class StockBasicRequest() extends Request {
  override val apiName: String = "stock_basic"
  override type Response = StockBasicResponse
}
