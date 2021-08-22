package pi

import org.rogach.scallop.*
import pi.ColorIterator.colorHue
import pi.Util as PiUtil

import java.awt.Color

object Main {

  case class Point(x: Double, y: Double) {

    def add(dx: Double, dy: Double): Point = Point(x + dx, y + dy)

  }

  case class Line(color: Color, start: Point, end: Point)

  trait Canvas {
    def size: Int

    def line(color: Color, from: Point, to: Point): Unit

    def close(): Unit
  }

  case class PiConfig(
                       id: String,
                       depth: Int,
                       width: Int,
                       stroke: Double,
                       background: Color,
                       colors: Iterator[Color],
                       createThumbnail: Boolean = true,
                     )

  val baseSize = 1080

  val cfgs = Seq(
    PiConfig("resa1", 1, 50, 1, Color.WHITE, ColorIterator.red),
    PiConfig("res2", 2, 7, 1, Color.WHITE, ColorIterator.red),
    PiConfig("demo1", 5, 1 * baseSize, 15, Color.BLACK, ColorIterator.pi(ColorIterator.seqColorHue)),
    PiConfig("pi-zero-7", 7, 2 * baseSize, 5, Color.WHITE, ColorIterator.pi(ColorIterator.seqZero)),
    PiConfig("ran-zero-7", 7, 2 * baseSize, 5, Color.WHITE, ColorIterator.random(ColorIterator.seqZero)),
    PiConfig("pi-zero-XL", 9, 5 * baseSize, 7, Color.WHITE, ColorIterator.pi(ColorIterator.seqZero)),
    PiConfig("pi-color-hue-XL", 9, 5 * baseSize, 4, Color.BLACK, ColorIterator.pi(ColorIterator.seqColorHue)),
    PiConfig("inc-color-hue-long-XL", 9, 5 * baseSize, 4, Color.BLACK, ColorIterator.increasing(PiUtil.linvals(10000, 0, 0.9999, colorHue))(9999)),
    PiConfig("inc-color-hue", 6, 1 * baseSize, 10, Color.BLACK, ColorIterator.increasing(ColorIterator.seqColorHue)(9))
  )


  class Conf(arguments: Array[String]) extends ScallopConf(arguments) {
    object draw extends Subcommand("draw") {
      def idVali(id: String): Boolean = cfgs.map(_.id).contains(id)

      def idDescrDescr(): String = {
        val ids = cfgs.map(_.id).mkString(", ")
        s"one of the following configuration IDs: $ids"
      }

      val id = trailArg[String](required = true, validate = idVali, descr = idDescrDescr())
    }

    object tryout extends Subcommand("tryout") {
    }

    addSubcommand(draw)
    addSubcommand(tryout)
    verify()
  }

  def main(args: Array[String]): Unit = {

    val conf = new Conf(args)
    conf.subcommand.map(_ match {
      case conf.draw =>
        val cfg = cfgs.filter(_.id == conf.draw.id()).head
        Drawing.run(cfg)

      case conf.tryout =>
        Tryout.analyseMinimalResolution()
    })
  }
}
