object CF1076c extends App {
  val in = new java.util.Scanner(System.in)
  val out = new java.io.PrintWriter(System.out)

  import scala.math._
  import scala.collection.mutable.{Queue, HashSet, HashMap}

  def edgeToList(edges: Seq[(Int, Int, Long)]) : Array[List[(Int, Long)]] = {
    val acc = Array.fill[List[(Int, Long)]](n)(List())
    edges foreach ((z) => {
      val (x,y,w) = z
      acc(x) = (y, w)::acc(x)
      acc(y) = (x, w)::acc(y)
    })
    acc
  }

  // if k <= n, exactly k point would be good
  // construct a tree that is minimum (respect to path) spanning tree
  // or shortest path to all node
  def shortest(n: Int, edges: Seq[List[(Int, Long)]]) : Array[(Int, Long)] = {
    val result = Array.fill[(Int, Long)](n)((-1, Long.MaxValue))
    result(0) = (0, 0);
    val (queue, set) = (Queue(0), HashSet(0))
    while (queue.nonEmpty) {
      val x = queue.dequeue
      set remove x
      edges(x) foreach ((z) => {
        val (y, w) = z
        if (result(x)._2 + w < result(y)._2) {
          result(y) = (x, result(x)._2 + w)
          if (!(set contains y)) {
            set += y
            queue += y
          }
        }
      })
    }
    result
  }

  def bfs(tree: Seq[(Int, Long)], k: Int) : List[(Int, Int)] = {
    val nodeList = edgeToList(tree.zipWithIndex.map({case ((y, w), x) => (x, y, w)}))
    val queue = Queue(0)
    var result = List[(Int, Int)]()
    while (queue.nonEmpty) {
      if (result.length >= k) {
        return result.take(k)
      }
      val x = queue.dequeue
      queue ++= nodeList(x).map(_._1)
      result = result ++ nodeList(x).map((z) => {(z._1, x)})
    }
    return result;
  }

  def orderBy(edges: Seq[(Int, Int)], allEdges: Seq[(Int, Int, Long)]) : List[Int] = {
    val map = HashMap[(Int, Int), Int]()
    allEdges.zipWithIndex.foreach({
      case ((x, y, _), i) => map += ((x, y) -> i)
    })
    edges.map(map(_)).toList.sorted
  }

  val (n, m, k) = (in.nextInt, in.nextInt, in.nextInt)
  val edges = 1 to m map ((_: Int) => {
    val (x, y, w) = (in.nextInt, in.nextInt, in.nextLong)
    (x-1, y-1, w)
  })
  // edges foreach (println(_))
  val nodeEdges = edgeToList(edges)
  // nodeEdges foreach (println(_))
  val tree = shortest(n, nodeEdges)
  val remainEdges = bfs(tree, k)
  val result = orderBy(remainEdges, edges)
  out.println(result.length)
  out.println(result.mkString(" "))
  out.flush;out.close;
}
