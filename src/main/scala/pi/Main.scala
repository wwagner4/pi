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

  lazy val cfgs = Seq(
    DrawConfig("demo1", 5, 1 * baseSize, 15, Color.BLACK, ColorIterator.iterator(Colors.Pi(DigiMap.Hue))),
    DrawConfig("pi-zero-7", 7, 2 * baseSize, 5, Color.WHITE, ColorIterator.iterator(Colors.Pi(DigiMap.ZeroBlack))),
    DrawConfig("ran-zero-7", 7, 2 * baseSize, 5, Color.WHITE, ColorIterator.iterator(Colors.Random(DigiMap.ZeroBlack))),
    DrawConfig("pi-zero-XL", 9, 5 * baseSize, 7, Color.WHITE, ColorIterator.iterator(Colors.Pi(DigiMap.ZeroBlack))),
    DrawConfig("pi-color-hue-XL", 9, 5 * baseSize, 4, Color.BLACK, ColorIterator.iterator(Colors.Pi(DigiMap.Hue))),
    DrawConfig("inc-color-hue-long-XL", 9, 5 * baseSize, 4, Color.BLACK, ColorIterator.iterator(Colors.Hue(10_000))),
    DrawConfig("inc-color-hue", 6, 1 * baseSize, 10, Color.BLACK, ColorIterator.iterator(Colors.Hue(10))),
    DrawConfig("pi-5-colorful", 5, 4000, 100, Color.BLACK, ColorIterator.iterator(Colors.Pi(DigiMap.Hue)), createThumbnails = true),
    {
      val depth = 10
      val size = PiUtil.minCanvasSizeForDepth(depth)
      val colors = ColorIterator.iterator(Colors.Pi(DigiMap.Hue))
      DrawConfig(s"minwidth-$depth-$size", depth, size, 1, Color.BLACK, colors, createThumbnails = true)
    },
    {
      val depth = 12
      val size = PiUtil.minCanvasSizeForDepth(depth)
      val colors = ColorIterator.iterator(Colors.Pi(DigiMap.Hue))
      DrawConfig(s"minwidth-$depth-$size", depth, size, 1, Color.BLACK, colors, createThumbnails = true)
    },
    {
      val depth = 12
      val size = PiUtil.minCanvasSizeForDepth(depth)
      val colors = ColorIterator.iterator(Colors.Random(DigiMap.Hue))
      DrawConfig(s"minwidth-random-$depth-$size", depth, size, 1, Color.BLACK, colors, createThumbnails = true)
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

    object hp extends Subcommand("hp") {
    }

    addSubcommand(draw)
    addSubcommand(drawall)
    addSubcommand(tryout)
    addSubcommand(hp)
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

      case conf.hp =>
        Hp.create()
    })
  }
}
