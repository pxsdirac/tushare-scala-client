package com.github.pxsdirac.tushare.cache.scylla

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.policies._

import java.io.Closeable
import java.util.concurrent.TimeUnit

class ScyllaSession(settings: ScyllaSettings) extends Closeable {
  private val policies: Policies = {
    // TODO: make this configurable
    val baseDelayMs = 50
    val maxDelayMs = TimeUnit.MINUTES.toMillis(1)
    Policies
      .builder()
      .withLoadBalancingPolicy(new TokenAwarePolicy(new RoundRobinPolicy))
      .withReconnectionPolicy(
        new ExponentialReconnectionPolicy(baseDelayMs, maxDelayMs)
      )
      .withRetryPolicy(new LoggingRetryPolicy(FallthroughRetryPolicy.INSTANCE))
      .build()
  }

  private val builder: Cluster.Builder =
    Cluster
      .builder()
      .addContactPoints(settings.contactPoints: _*)
      .withPort(settings.port)
      .withLoadBalancingPolicy(policies.getLoadBalancingPolicy)
      .withReconnectionPolicy(policies.getReconnectionPolicy)
      .withRetryPolicy(policies.getRetryPolicy)
      .withSpeculativeExecutionPolicy(policies.getSpeculativeExecutionPolicy)
      .withTimestampGenerator(policies.getTimestampGenerator)
      .withoutJMXReporting()

  val cluster = builder.build()
  val session = cluster.connect(settings.keyspace)

  override def close(): Unit = {
    session.close()
    cluster.close()
  }
}
