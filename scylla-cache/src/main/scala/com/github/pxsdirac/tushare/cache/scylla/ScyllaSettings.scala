package com.github.pxsdirac.tushare.cache.scylla

import com.datastax.driver.core.ConsistencyLevel

case class ScyllaSettings(
    contactPoints: Seq[String],
    keyspace: String,
    port: Int,
    readConsistency: ConsistencyLevel,
    writeConsistency: ConsistencyLevel
)
