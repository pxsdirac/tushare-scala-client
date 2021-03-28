package com.github.pxsdirac.tushare.api

case class MarginResponse(
    trade_date: String,
    exchange_id: String,
    rzye: Double,
    rzmre: Double,
    rzche: Double,
    rqye: Double,
    rqmcl: Double,
    rzrqye: Double,
    rqyl: Double
)
