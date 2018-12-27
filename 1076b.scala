object CF1076b extends App {
  val in = new java.util.Scanner(System.in)
  val out = new java.io.PrintWriter(System.out)

  import scala.math._

  def findPrime(n: Long) : Long = {
    if (n % 2 == 0) { 2 }
    else {
      (3 to Int.MaxValue by 2).view.map(_.toLong).takeWhile((x: Long) => x*x <= n).filter(n % _ == 0).headOption getOrElse n
    }
  }

  val n = in.nextLong
  out.println(n/findPrime(n))
  out.flush;out.close;
}
