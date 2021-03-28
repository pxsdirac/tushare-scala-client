package com.github.pxsdirac.tushare.core

trait Request {
  val apiName: String
  type Response
}
