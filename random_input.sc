import scala.collection.mutable.HashSet
import scala.util.Random

val (n, m, k) = (100000, 300000, 50000)
val set = HashSet((0, 0))

def generate(x0: Int = -1) : Seq[Long] = {
  val (x, y) = if (x0 == -1) { (rand.nextInt(n), rand.nextInt(n)) } else { (rand.nextInt(x0), x0) }
  val w = (rand.nextInt(Int.MaxValue))
  if (w != 0 && x != y && set.add((x,y)) && set.add((y,x))) {
    Seq(x+1, y+1, w)
  } else {
    generate(x0)
  }
}

val rand = new Random
println(Seq(n, m, k).mkString(" "))
1 to (n-1) foreach ((x) =>
  println(generate(x).mkString(" "))
)
n to m foreach ((_) => {
  println(generate().mkString(" "))
})
