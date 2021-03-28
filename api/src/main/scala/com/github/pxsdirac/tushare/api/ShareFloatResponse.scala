package com.github.pxsdirac.tushare.api

case class ShareFloatResponse(
    ts_code: String,
    ann_date: Option[String],
    float_date: String,
    float_share: Double,
    float_ratio: Double,
    holder_name: String,
    share_type: String
)
