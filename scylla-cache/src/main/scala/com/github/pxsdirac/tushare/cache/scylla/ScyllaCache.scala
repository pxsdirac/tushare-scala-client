package com.github.pxsdirac.tushare.cache.scylla

import java.util.concurrent.Executor

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.control.NonFatal

import com.github.pxsdirac.tushare.core.cache.Cache
import com.google.common.util.{concurrent => guava}

object ScyllaCache {
  implicit def toExecutor(implicit ec: ExecutionContext): Executor =
    (command: Runnable) => {
      ec.execute(new Runnable {
        def run(): Unit = {
          try command.run()
          catch {
            case NonFatal(ex) => ec.reportFailure(ex)
          }
        }
      })
    }
  implicit class GuavaFutureOps[T](lf: guava.ListenableFuture[T]) {
    def asScala(implicit ec: ExecutionContext): Future[T] = {
      val p = Promise[T]()

      guava.Futures.addCallback(
        lf,
        new guava.FutureCallback[T] {
          def onFailure(e: Throwable): Unit = p tryFailure e
          def onSuccess(t: T): Unit = p trySuccess t
        },
        toExecutor(ec)
      )

      p.future
    }
  }
}
class ScyllaCache(settings: ScyllaSettings)(implicit
    executionContext: ExecutionContext
) extends Cache {
  import ScyllaCache._
  val scyllaClient = new ScyllaSession(settings)
  val saveQuery = "insert into cache(key,value) values(?,?)"
  val getQuery = "select * from cache where key = ?"
  val savePS = scyllaClient.session.prepare(saveQuery)
  val getPs = scyllaClient.session.prepare(getQuery)

  override def save(key: String, value: String): Future[Unit] = {
    val s = savePS.bind().setString("key", key).setString("value", value)
    scyllaClient.session
      .executeAsync(s)
      .asScala
      .map(_ => ())
  }

  override def get(key: String): Future[Option[String]] = {
    val s = getPs.bind().setString("key", key)
    import scala.collection.JavaConverters._
    scyllaClient.session
      .executeAsync(s)
      .asScala
      .map(_.all().asScala.headOption.map(_.getString("value")))
  }

  override def close(): Unit = scyllaClient.close()
}
