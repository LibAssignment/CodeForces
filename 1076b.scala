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

  def solve(n: Long) : Long = {
    if (n % 2 == 0) { n/2 }
    else {
      solve(n-findPrime(n)) + 1
    }
  }

  val n = in.nextLong
  out.println(solve(n))
  out.flush;out.close;
}
