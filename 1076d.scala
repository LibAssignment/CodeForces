object CF1076d extends App {
  import java.io.{InputStream, InputStreamReader, BufferedReader}
  import java.util.StringTokenizer
  // https://codeforces.com/blog/entry/21074
  class ReadScanner(reader: BufferedReader, radix: Int = 10) extends Iterator[String] with AutoCloseable {
    def this(stream: InputStream) = this(new BufferedReader(new InputStreamReader(stream)))
    private[this] val tokenizers = Iterator.continually(reader.readLine()).takeWhile(_ != null).map(new StringTokenizer(_))
    private[this] var current: Option[StringTokenizer] = None
    @inline private[this] def tokenizer(): Option[StringTokenizer] = current.find(_.hasMoreTokens) orElse {
      current = if (tokenizers.hasNext) Some(tokenizers.next()) else None; current
    }

    override def next() = tokenizer().get.nextToken()
    override def hasNext = tokenizer().nonEmpty
    override def close() = reader.close()
    def line(): String = tokenizer().get.nextToken("\n\r")
    def nextString(): String = next()
    def nextChar(): Char = next().ensuring(_.length == 1).head
    def nextBoolean(): Boolean = next().toBoolean
    def nextByte(): Byte = java.lang.Byte.parseByte(next(), radix)
    def nextShort(): Short = java.lang.Short.parseShort(next(), radix)
    def nextInt(): Int = java.lang.Integer.parseInt(next(), radix)
    def nextLong(): Long = java.lang.Long.parseLong(next(), radix)
    def nextBigInt(): BigInt = BigInt(next(), radix)
    def nextFloat(): Float = next().toFloat
    def nextDouble(): Double = next().toDouble
    def nextBigDecimal(): BigDecimal = BigDecimal(next())
  }
  val startTime = System.currentTimeMillis()
  val in = new ReadScanner(System.in)
  val out = new java.io.PrintWriter(System.out)

  import scala.math._
  import scala.collection.mutable.{Queue, HashSet, HashMap, ListBuffer}

  def edgeToList(edges: Seq[(Int, Int, Long, Int)], direction: Boolean = false) : Seq[ListBuffer[(Int, Long, Int)]] = {
    val acc = Array.fill(n)(ListBuffer[(Int, Long, Int)]())
    edges foreach ({case (x,y,w,i) =>
      acc(x) += ((y, w, i))
      if (!direction) {
        acc(y) += ((x, w, i))
      }
    })
    acc// map (_.toList)
  }

  // if k <= n, exactly k point would be good
  // construct a tree that is minimum (respect to path) spanning tree
  // or shortest path to all node
  def shortest(n: Int, edges: Seq[ListBuffer[(Int, Long, Int)]]) : Array[(Int, Long, Int)] = {
    val result = Array.fill[(Int, Long, Int)](n)((-1, Long.MaxValue, 0))
    result(0) = (0, 0, 0);
    val (queue, set) = (Queue(0), HashSet(0))
    while (queue.nonEmpty) {
      val x = queue.dequeue
      set remove x
      edges(x) foreach ({case (y, w, i) =>
        if (result(x)._2 + w < result(y)._2) {
          result(y) = (x, result(x)._2 + w, i)
          if (set add y) {
            queue enqueue y
          }
        }
      })
    }
    result
  }

  def bfs(tree: Seq[(Int, Long, Int)], k: Int) : List[(Int, Int, Int)] = {
    val nodeList = edgeToList(tree.zipWithIndex.drop(1).map({case ((y, w, i), x) => (y, x, w, i)}), true)
    // nodeList foreach println
    val queue = Queue(0)
    var result = ListBuffer[(Int, Int, Int)]()
    while (queue.nonEmpty && result.length < k) {
      val x = queue.dequeue
      queue ++= nodeList(x).map(_._1)
      result ++= nodeList(x).map({case (y, _, i) => (y, x, i)})
    }
    return result.toList.take(k);
  }

  val (n, m, k) = (in.nextInt, in.nextInt, in.nextInt)
  val edges = 1 to m map ((i) => {
    val (x, y, w) = (in.nextInt, in.nextInt, in.nextLong)
    (x-1, y-1, w, i)
  })
  println("edges:", System.currentTimeMillis()-startTime)
  // edges foreach (println(_))

  val nodeEdges = edgeToList(edges)
  println("nodeEdges:", System.currentTimeMillis()-startTime)
  // nodeEdges foreach (println(_))

  val tree = shortest(n, nodeEdges)
  println("spfa:", System.currentTimeMillis()-startTime)
  // tree foreach println

  val remainEdges = bfs(tree, k)
  println("bfs:", System.currentTimeMillis()-startTime)
  // remainEdges foreach println

  out.println(remainEdges.length)
  out.println(remainEdges.map(_._3).mkString(" ").length)
  println("mkString:", System.currentTimeMillis()-startTime)
  out.flush;out.close;
}
