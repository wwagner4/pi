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

  case class DrawConfig(
                         id: String,
                         depth: Int,
                         size: Int,
                         stroke: Double,
                         background: Color,
                         colors: Iterator[Color],
                         createThumbnails: Boolean = false,
                     )

  val baseSize = 1080

  val cfgs = Seq(
    DrawConfig("demo1", 5, 1 * baseSize, 15, Color.BLACK, ColorIterator.pi(ColorIterator.seqColorHue)),
    DrawConfig("pi-zero-7", 7, 2 * baseSize, 5, Color.WHITE, ColorIterator.pi(ColorIterator.seqZero)),
    DrawConfig("ran-zero-7", 7, 2 * baseSize, 5, Color.WHITE, ColorIterator.random(ColorIterator.seqZero)),
    DrawConfig("pi-zero-XL", 9, 5 * baseSize, 7, Color.WHITE, ColorIterator.pi(ColorIterator.seqZero)),
    DrawConfig("pi-color-hue-XL", 9, 5 * baseSize, 4, Color.BLACK, ColorIterator.pi(ColorIterator.seqColorHue)),
    DrawConfig("inc-color-hue-long-XL", 9, 5 * baseSize, 4, Color.BLACK, ColorIterator.increasingHue(10000)),
    DrawConfig("inc-color-hue", 6, 1 * baseSize, 10, Color.BLACK, ColorIterator.increasing10(ColorIterator.seqColorHue)),
    DrawConfig("pi-5-colorful", 5, 4000, 100, Color.BLACK, ColorIterator.pi(ColorIterator.seqColorHue), createThumbnails = true),
    {
      val depth = 10
      val size = PiUtil.minCanvasSizeForDepth(depth)
      val colors = ColorIterator.pi (ColorIterator.seqColorHue)
      DrawConfig(s"minwidth-$depth-$size", depth, size, 1, Color.BLACK, colors, createThumbnails = true)
    }
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

    object drawall extends Subcommand("drawall") {
    }

    object tryout extends Subcommand("tryout") {
    }

    addSubcommand(draw)
    addSubcommand(drawall)
    addSubcommand(tryout)
    verify()
  }

  def main(args: Array[String]): Unit = {

    val conf = new Conf(args)
    conf.subcommand.map(_ match {
      case conf.draw =>
        val cfg = cfgs.filter(_.id == conf.draw.id()).head
        Drawing.run(cfg)

      case conf.drawall =>
        cfgs.foreach(cfg => Drawing.run(cfg))

      case conf.tryout =>
        Tryout.doIt()
    })
  }
}
