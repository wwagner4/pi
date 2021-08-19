package pi

import java.awt.Color

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
                       colors: Iterator[Color]
                     )

  val bsae = 1080

  val cfgs = Seq(
    PiConfig("pi-zero-7", 7, 2 * bsae, 5, ColorIterator.pi(ColorIterator.seqZero)),
    PiConfig("ran-zero-7", 7, 2 * bsae, 5, ColorIterator.random(ColorIterator.seqZero)),
    PiConfig("pi-zero-XL", 9, 5 * bsae, 7, ColorIterator.pi(ColorIterator.seqZero))
  )

  def main(args: Array[String]): Unit = {
    val cfg = cfgs.filter(_.id == "pi-zero-XL").head
    Drawing.run(cfg)
  }

}
