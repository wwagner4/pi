package pi

import java.awt.Color
import scala.io.Source
import scala.util.Random

object ColorIterator {

  def colorHue(hue: Double): Color = {
    Color.getHSBColor(hue.toFloat, 1.0f, 1.0f)
  }

  def colorBright(hue: Double)(bright: Double): Color = {
    Color.getHSBColor(hue.toFloat, 1.0f, bright.toFloat)
  }

  lazy val seqColorBrightRed = Util.linvals(10, 0, 1, colorBright(0))
  lazy val seqColorBrightGreen = Util.linvals(10, 0, 1, colorBright(0.3))
  lazy val seqColorBright3 = Util.linvals(10, 0, 1, colorBright(0.7))

  lazy val seqColorHue = Util.linvals(10, 0, 0.9, colorHue)

  lazy val seqZero = Seq(
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
  def gray: Iterator[Color] = LazyList.continually(Color.GRAY).iterator

  def random(colors: Seq[Color]): Iterator[Color] = {
    new Iterator[Color] {
      override def next() = {
        val i = Random.nextInt(colors.length)
        colors(i)
      }

      override def hasNext() = true
    }
  }

  def increasing10 = increasing(10)

  private def increasing(size: Int)(colors: Seq[Color]): Iterator[Color] = {
    val seq = 0 to (size - 1)
    LazyList.continually(seq).flatten.iterator.map(colors(_))
  }

  def increasingHue(size: Int): Iterator[Color] = {
    val colors = Util.linvals(10000, 0, 0.9999, colorHue)
    val seq = 0 to (size - 1)
    LazyList.continually(seq).flatten.iterator.map(colors(_))
  }

  def pi(colors: Seq[Color]): Iterator[Color] = {
    require(colors.size == 10)
    Util.digitsPiXL.map(colors(_))
  }


}
