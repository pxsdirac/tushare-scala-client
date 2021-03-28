## Tushare Scala Client

### 1. 简介
该库旨在基于[Tushare](https://tushare.pro/) 的[http接口](https://tushare.pro/document/1?doc_id=130) ,使用scala语法进行封装，提供一个类型安全,易于使用和扩展的接口。

### 2. 导入
目前还未上传jar包到maven仓库, 需克隆项目手动编译:
```
git clone git@github.com:pxsdirac/tushare-scala-client.git
cd tushare-scala-client
sbt publishLocal
```

然后，在你的sbt项目中引用:
```
val version = // 编译出的版本号
libraryDependencies ++= Seq(
    "com.github.pxsdirac" %% "core" % version,
    "com.github.pxsdirac" %% "api" % version,
    "com.github.pxsdirac" %% "scylla-cache" % version //可选，仅在需要cache时导入
}
```
> 该库使用了一些较为复杂的scala特性，不大适合在java中使用

#### 2.1 各依赖介绍
##### 2.1.1 core
提供该库的核心功能, 理论上只需要导入该包即可。但一般情况下可以搭配`api`包进行使用。
##### 2.1.2 api
提供预定义的各api的请求和响应的数据类型, 用户也可以自己定义。由于积分问题，未对所有接口进行测试，在后期将逐渐增加，也欢迎大家贡献代码。
##### 2.1.3 scylla-cache
使用scylla对请求结果进行缓存,以减少请求tushare服务的次数。可选。用户也可以根据[接口](core/src/main/scala/com/github/pxsdirac/tushare/core/cache/Cache.scala)自行实现。

### 3.使用
#### 3.1 token
token默认从`~/.tushare/token`文件加载，你可以将你的token保存在该文件中。用户也可以自行在程序中定义token
#### 3.2 api使用
```scala
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
```

### 4. 未来方向
该库基本功能已经实现，可以进行使用，但仍有许多需要完善的地方。欢迎大家来贡献代码。
#### 4.1 core
* 基于akka stream提供流量控制功能
* 基于akka stream通过自动翻页功能(部分请求无法在一个结果内返回)
#### 4.2 api
* 增加tushare中已有的api接口
* Request和Response内变量定义支持驼峰格式。目前只变量名需和http接口中的字段名严格一致。
#### 4.3 cache
* 提供更多基于其它存储的接口
* 寻求更合理的cache方式，目前cache的key为请求的类型以及请求的内容, 对于部分请求, 可能导致请求相同，但是内容已经改变。比如不指定`end_date`请求`stock_basic`接口。