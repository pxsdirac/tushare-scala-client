package com.github.pxsdirac.tushare.api

case class Top10HoldersResponse(
    ts_code: String,
    ann_date: String,
    end_date: String,
    holder_name: String,
    hold_amount: Double,
    hold_ratio: Double
)
