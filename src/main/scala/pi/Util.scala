package pi

import java.nio.file.Path
import scala.io.Source

object Util {

  def linvals[T](n: Int, y1: Double, y2: Double, f: Double => T): Seq[T] = {
    val x1 = 0
    val x2 = n - 1
    val l: Int => Double = i => y1 + (y2 - y1) / (x2 - x1) * i
    (x1 to x2).map(i => f(l(i)))
  }

  def digitsFromFile(path: Path): Iterator[Int] = {
    Source.fromFile(path.toFile)
      .iterator
      .filter(_.isDigit)
      .map(_.asInstanceOf[Int])
  }

  def digitsPi1mFromResource: Iterator[Int] = {
    Source.fromResource("pi-dec-1m.txt")
      .iterator
      .filter(_.isDigit)
      .map(_.asInstanceOf[Int])
  }


}
