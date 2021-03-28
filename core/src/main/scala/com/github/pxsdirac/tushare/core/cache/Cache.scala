package com.github.pxsdirac.tushare.core.cache

import java.io.Closeable
import scala.concurrent.Future

trait Cache extends Closeable {
  def save(key: String, value: String): Future[Unit]
  def get(key: String): Future[Option[String]]
}

object Cache {
  lazy val noCache: Cache = new Cache {
    override def save(key: String, value: String): Future[Unit] = Future.unit

    override def get(key: String): Future[Option[String]] =
      Future.successful(None)

    override def close(): Unit = ()
  }
}
