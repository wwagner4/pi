package pi

import java.nio.file.{Files, Path}
import scala.io.Source

object Util {

  def minCanvasSizeForDepth(depth: Int): Int = {
    math.pow(2, depth + 1).toInt + 1
  }

  def linvals[T](n: Int, y1: Double, y2: Double, f: Double => T): Seq[T] = {
    val x1 = 0
    val x2 = n - 1
    val l: Int => Double = i => y1 + (y2 - y1) / (x2 - x1) * i
    (x1 to x2).map(i => f(l(i)))
  }

  def digitsPiXL: Iterator[Int] = {
    val home = System.getProperty("user.home")
    val work = Path.of(home, "work", "pi")
    val piFile = work.resolve("pi.txt")
    require(Files.exists(piFile), s"file must exist $piFile")
    Source.fromFile(piFile.toFile)
      .iterator
      .filter(_.isDigit)
      .map(_.asDigit)
  }

  def digitsPi1mFromResource: Iterator[Int] = {
    Source.fromResource("pi-dec-1m.txt")
      .iterator
      .filter(_.isDigit)
      .map(_.asDigit)
  }


}
