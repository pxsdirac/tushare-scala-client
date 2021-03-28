import akka.actor.ActorSystem

import com.datastax.driver.core.ConsistencyLevel
import com.github.pxsdirac.tushare.api.StockBasicRequest
import com.github.pxsdirac.tushare.cache.scylla.{ScyllaCache, ScyllaSettings}
import com.github.pxsdirac.tushare.core.Client

import scala.util.{Failure, Success}

object StockBasicExample extends App {
  implicit val actorSystem = ActorSystem("tushare")
  import actorSystem.dispatcher
  import io.circe.generic.auto._

  val scyllaSettings = ScyllaSettings(
    contactPoints = Seq("127.0.0.1"),
    keyspace = "tushare",
    port = 9042,
    readConsistency = ConsistencyLevel.ALL,
    writeConsistency = ConsistencyLevel.ALL
  )
  implicit val cache: ScyllaCache = new ScyllaCache(scyllaSettings)

  val request = StockBasicRequest()

  Client.request(request).onComplete {
    case Success(value)     => value foreach println
    case Failure(exception) => exception.printStackTrace()
  }
}
