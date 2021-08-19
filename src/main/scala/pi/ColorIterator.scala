package pi

import java.awt.Color
import scala.io.Source
import scala.util.Random

object ColorIterator {

  val seqColored = Seq(
    Color.BLACK,
    Color.GRAY,
    Color.GREEN,
    Color.YELLOW,
    Color.ORANGE,
    Color.RED,
    Color.CYAN,
    Color.BLUE,
    Color.MAGENTA,
    Color.PINK,
  )
  val seqZero = Seq(
    Color.BLACK,
    Color.LIGHT_GRAY,
    Color.LIGHT_GRAY,
    Color.LIGHT_GRAY,
    Color.LIGHT_GRAY,
    Color.LIGHT_GRAY,
    Color.LIGHT_GRAY,
    Color.LIGHT_GRAY,
    Color.LIGHT_GRAY,
    Color.LIGHT_GRAY,
  )

  def red: Iterator[Color] = LazyList.continually(Color.RED).iterator

  def random(colors: Seq[Color]): Iterator[Color] = {
    new Iterator[Color] {
      override def next() = {
        val i = Random.nextInt(colors.length)
        colors(i)
      }

      override def hasNext() = true
    }
  }

  def pi(colors: Seq[Color]): Iterator[Color] = {
    Source.fromResource("pi-dec-1m.txt")
      .iterator
      .filter(_.isDigit)
      .map(i => colors(i.asDigit))
  }


}
