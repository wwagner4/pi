package pi

import java.awt.Color
import scala.util.Random

object ColorIterator {

  val colorSeq = Seq(
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

  def red: Iterator[Color] = LazyList.continually(Color.RED).iterator

  def random: Iterator[Color] = {
    new Iterator[Color] {
      override def next() = {
        val i = Random.nextInt(colorSeq.length)
        colorSeq(i)
      }

      override def hasNext() = true
    }
  }

}
