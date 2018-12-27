object CF1076c extends App {
  val in = new java.util.Scanner(System.in)
  val out = new java.io.PrintWriter(System.out)

  import scala.math._

  // a + b == d && a * b == d
  // a, b is the root of x^2 - d x + d == 0
  def resolve(d: Double) : Option[(Double, Double)] = {
    if (d <= 0 || d >= 4) {
      val del = d*d/4-d
      Some(d/2-sqrt(del), d/2+sqrt(del))
    } else {
      None
    }
  }

  val n = in.nextInt
  1 to n foreach ((_: Int) => {
    val d = in.nextInt
    resolve(d.toDouble) match {
      case Some((a, b)) => println(s"Y $a $b")
      case None => println("N")
    }
  })
  out.flush;out.close;
}
