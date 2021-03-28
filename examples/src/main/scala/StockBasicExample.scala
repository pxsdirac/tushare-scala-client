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
  // 若不需要cache, 将cache换成以下的实现
  // implicit val cache = com.github.pxsdirac.tushare.core.cache.Cache.noCache

  // 若不使用~/.tushare/token中的值, 可使用以下方式定义token
  // implicit val token = com.github.pxsdirac.tushare.core.Token("your token")

  val request = StockBasicRequest()

  Client.request(request).onComplete {
    case Success(value)     => value foreach println
    case Failure(exception) => exception.printStackTrace()
  }
}
