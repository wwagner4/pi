package pi

import java.awt.Color
import scala.io.Source
import scala.util.Random

object ColorIterator {

  def colorHue(hue: Double): Color = {
    Color.getHSBColor(hue.toFloat, 1.0f, 1.0f)
  }

  def colorBright(bright: Double)(hue: Double): Color = {
    Color.getHSBColor(hue.toFloat, 1.0f, bright.toFloat)
  }

  lazy val seqColorHue = Util.linvals(10, 0, 0.9, colorHue)

  lazy val seqColorBright1 = Util.linvals(10, 0, 1, colorBright(0.3))

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

  def random(colors: Seq[Color]): Iterator[Color] = {
    new Iterator[Color] {
      override def next() = {
        val i = Random.nextInt(colors.length)
        colors(i)
      }

      override def hasNext() = true
    }
  }

  def increasing(colors: Seq[Color])(max: Int): Iterator[Color] = {
    val seq = 0 to max
    LazyList.continually(seq).flatten.iterator.map(colors(_))
  }

  def pi(colors: Seq[Color]): Iterator[Color] = {
    Util.digitsPi1mFromResource.map(colors(_))
  }


}
