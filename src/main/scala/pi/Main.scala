package pi

import pi.ColorIterator.colorHue

import java.awt.Color

import org.rogach.scallop._

object Main {

  case class Point(x: Double, y: Double) {

    def add(dx: Double, dy: Double): Point = Point(x + dx, y + dy)

  }

  case class Line(color: Color, start: Point, end: Point)

  trait Canvas {
    def width: Int

    def height: Int

    def line(color: Color, from: Point, to: Point): Unit

    def close(): Unit
  }

  case class PiConfig(
                       id: String,
                       depth: Int,
                       width: Int,
                       stroke: Double,
                       background: Color,
                       colors: Iterator[Color]
                     )

  val baseSize = 1080

  val cfgs = Seq(
    PiConfig("pi-zero-7", 7, 2 * baseSize, 5, Color.WHITE, ColorIterator.pi(ColorIterator.seqZero)),
    PiConfig("ran-zero-7", 7, 2 * baseSize, 5, Color.WHITE, ColorIterator.random(ColorIterator.seqZero)),
    PiConfig("pi-zero-XL", 9, 5 * baseSize, 7, Color.WHITE, ColorIterator.pi(ColorIterator.seqZero)),
    PiConfig("pi-color-hue-XL", 9, 5 * baseSize, 4, Color.BLACK, ColorIterator.pi(ColorIterator.seqColorHue)),
    PiConfig("inc-color-hue-long-XL", 9, 5 * baseSize, 4, Color.BLACK, ColorIterator.increasing(ColorIterator.linvals(10000, 0, 0.9999, colorHue))(9999)),
    PiConfig("inc-color-hue", 6, 1 * baseSize, 10, Color.BLACK, ColorIterator.increasing(ColorIterator.seqColorHue)(9))
  )

  class Conf(arguments: Array[String]) extends ScallopConf(arguments) {

    def piConfigValidation(id: String): Boolean = {
      cfgs.map(_.id).contains(id)
    }
    def piConfigDescr(): String = {
      val ids = cfgs.map(_.id).mkString(", ")
      s"one of [$ids]"
    }

    val piconfig = trailArg[String](name ="id", required = true, validate = piConfigValidation, descr=piConfigDescr())
    verify()
  }

  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)
    val cfg = cfgs.filter(_.id == conf.piconfig()).head
    Drawing.run(cfg)
  }
}
