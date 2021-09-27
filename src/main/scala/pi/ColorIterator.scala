package pi

import java.awt.Color
import scala.io.Source
import scala.util.Random

sealed trait DigiMap

object DigiMap {

  case object Hue extends DigiMap

  case object ZeroBlack extends DigiMap

}

sealed trait Colors

object Colors {

  case object Red extends Colors

  case class Pi(digiMap: DigiMap) extends Colors

  case class Random(digiMap: DigiMap) extends Colors

  case class Hue(length: Int) extends Colors

}


object ColorIterator {

  def iterator(colors: Colors): Iterator[Color] =
    colors match {
      case Colors.Red => red
      case Colors.Pi(digiMap) => digiMap match {
        case DigiMap.Hue => pi(seqColorHue)
        case DigiMap.ZeroBlack => pi(seqZero)
      }
      case Colors.Random(digiMap) => digiMap match {
        case DigiMap.Hue => random(seqColorHue)
        case DigiMap.ZeroBlack => random(seqZero)
      }
      case Colors.Hue(length) => increasingHue(length)
    }


  private def colorHue(hue: Double): Color = {
    Color.getHSBColor(hue.toFloat, 1.0f, 1.0f)
  }

  private lazy val seqColorHue = Util.linvals(10, 0, 0.9, colorHue)

  private lazy val seqZero = Seq(
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

  private def red: Iterator[Color] = LazyList.continually(Color.RED).iterator

  private def gray: Iterator[Color] = LazyList.continually(Color.GRAY).iterator

  private def random(colors: Seq[Color]): Iterator[Color] = {
    new Iterator[Color] {
      override def next() = {
        val i = Random.nextInt(colors.length)
        colors(i)
      }

      override def hasNext() = true
    }
  }

  private def increasing(size: Int)(colors: Seq[Color]): Iterator[Color] = {
    val seq = 0 to (size - 1)
    LazyList.continually(seq).flatten.iterator.map(colors(_))
  }

  private def increasingHue(size: Int): Iterator[Color] = {
    val colors = Util.linvals(size, 0, 1.0 - 1.0 / size, colorHue)
    val seq = 0 to (size - 1)
    LazyList.continually(seq).flatten.iterator.map(colors(_))
  }

  private def pi(colors: Seq[Color]): Iterator[Color] = {
    require(colors.size == 10)
    Util.digitsPiXL.map(colors(_))
  }


}
