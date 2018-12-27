object CF1076c extends App {
  val in = new java.util.Scanner(System.in)
  val out = new java.io.PrintWriter(System.out)

  import scala.math._

  // if k <= n, exactly k point would be good
  // construct a tree that is minimum (respect to path) spanning tree
  // or shortest path to all node
  def shortest(n: Int, edges: Seq[(Int, Int, Long)]) : Unit = {

  }

  val (n, m, k) = (in.nextInt, in.nextInt, in.nextInt)
  val edges = 1 to m map ((_: Int) => {
    (in.nextInt, in.nextInt, in.nextLong)
  })
  val nodeEdges = edges.foldLeft(Array.fill[List[(Int, Long)]](n)(List()))((acc, z) => { val (x,y,w) = z; acc(x-1) = (y-1, w)::acc(x-1); acc(y-1) = (x-1, w)::acc(y-1); acc })
  edges foreach (println(_))
  println("------")
  nodeEdges foreach (println(_))
  out.flush;out.close;
}
