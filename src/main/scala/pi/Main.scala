package pi

import pi.ColorIterator.colorHue
import pi.Util as PiUtil
import scopt.OParser

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

  trait ConfigWithId {
    def id: String

    def description: String
  }

  case class TilesConfig(
                          prescaling: Int = 2,
                          zoomLevels: String = "1-6",
                          rightMarginEm: Double = 0.8,
                          initialResolution: Double = 8.0
                        )

  case class DrawConfig(
                         id: String,
                         depth: Int,
                         size: Int,
                         stroke: Double,
                         background: Color,
                         colors: () => Iterator[Color],
                         description: String = "",
                         tilesConfig: TilesConfig = TilesConfig()
                       ) extends ConfigWithId


  val baseSize = 1080

  lazy val cfgs = Seq(
    DrawConfig(
      id = "demo",
      description = "Simple demo",
      depth = 5,
      size = 1 * baseSize,
      stroke = 15,
      background = Color.BLACK,
      colors = () => ColorIterator.iterator(Colors.Pi(DigiMap.Hue)),
      tilesConfig = TilesConfig(
        prescaling = 8,
        initialResolution = 9.0,
      )
    ),
    DrawConfig(
      id = "demo1",
      description = "Simple demo",
      depth = 7,
      size = 1 * baseSize,
      stroke = 5,
      background = Color.BLACK,
      colors = () => ColorIterator.iterator(Colors.Hue(8_000)),
      tilesConfig = TilesConfig(
        prescaling = 16,
        initialResolution = 18.5,
      )
    ),
    DrawConfig(
      id = "piz2",
      description = "Pi zero shallow",
      depth = 7,
      size = 2 * baseSize,
      stroke = 5,
      background = Color.WHITE,
      colors = () => ColorIterator.iterator(Colors.Pi(DigiMap.ZeroBlack)),
      tilesConfig = TilesConfig(
        prescaling = 8,
        initialResolution = 10,
      )
    ),
    DrawConfig(
      id = "ran1",
      description = "Random zero shallow",
      depth = 7,
      size = 2 * baseSize,
      stroke = 5,
      background = Color.WHITE,
      colors = () => ColorIterator.iterator(Colors.Random(DigiMap.ZeroBlack)),
    ),
    DrawConfig(
      id = "pi1",
      description = "Pi zero deep",
      depth = 9,
      size = 5 * baseSize,
      stroke = 7,
      background = Color.WHITE,
      colors = () => ColorIterator.iterator(Colors.Pi(DigiMap.ZeroBlack))
    ),
    DrawConfig(
      id = "pi2",
      description = "Pi hue deep",
      depth = 9,
      size = 5 * baseSize,
      stroke = 4,
      background = Color.BLACK,
      colors = () => ColorIterator.iterator(Colors.Pi(DigiMap.Hue))
    ),
    DrawConfig(
      id = "pi3",
      description = "Pi hue shallow",
      depth = 5,
      size = 4000,
      stroke = 100,
      background = Color.BLACK,
      colors = () => ColorIterator.iterator(Colors.Pi(DigiMap.Hue)),
    ),
    DrawConfig(
      id = "cf1",
      description = "Colorful deep",
      depth = 9,
      size = 5 * baseSize,
      stroke = 4,
      background = Color.BLACK,
      colors = () => ColorIterator.iterator(Colors.Hue(10_000)),
      tilesConfig = TilesConfig(
        prescaling = 2,
        zoomLevels = "1-6",
        rightMarginEm = 0.8,
      )
    ),
    DrawConfig(
      id = "cf2",
      description = "Colorful shallow",
      depth = 6,
      size = 1 * baseSize,
      stroke = 10,
      background = Color.BLACK,
      colors = () => ColorIterator.iterator(Colors.Hue(1000)),
      tilesConfig = TilesConfig(
        prescaling = 8,
        zoomLevels = "1-6",
        rightMarginEm = 1.0,
      )
    ),
    {
      val depth = 10
      DrawConfig(
        id = s"mwpi1",
        description = "Pi minwidth shallow",
        depth = depth,
        size = PiUtil.minCanvasSizeForDepth(depth),
        stroke = 1,
        background = Color.BLACK,
        colors = () => ColorIterator.iterator(Colors.Pi(DigiMap.Hue)),
        tilesConfig = TilesConfig(
          initialResolution = 2,
        )
      )
    },
    {
      val depth = 12
      DrawConfig(
        id = s"mwpi2",
        description = "Pi minwidth deep",
        depth = depth,
        size = PiUtil.minCanvasSizeForDepth(depth),
        stroke = 1,
        background = Color.BLACK,
        colors = () => ColorIterator.iterator(Colors.Pi(DigiMap.Hue)),
      )
    },
    {
      val depth = 12
      DrawConfig(
        id = s"mwran",
        description = "Random minwidth deep",
        depth = depth,
        size = PiUtil.minCanvasSizeForDepth(depth),
        stroke = 1,
        background = Color.BLACK,
        colors = () => ColorIterator.iterator(Colors.Random(DigiMap.Hue)),
      )
    },
  )


  sealed trait Command

  case class DrawCommand(id: String, tiles: Boolean, thumbnails: Boolean) extends Command

  case object TryoutCommand extends Command

  case object HpCommand extends Command

  case object ErrorCommand extends Command

  case object ServerCommand extends Command

  def parse(args: Array[String]): Command = {

    def table(selectables: Seq[ConfigWithId], withHeader: Boolean) = {
      val headerId = "id"
      val headerDescription = "description"

      def maxlen(strings: Iterable[String]): Int = strings.map(_.length).max

      def line(len: Int): String = Seq.fill(len)('-').mkString

      val lenId = math.max(maxlen(selectables.map(_.id)), headerId.length)
      val lenDesc = math.max(maxlen(selectables.map(_.description)), headerDescription.length)
      if withHeader then {
        val formatString = s"%-${lenId}s | %-${lenDesc}s"
        val header = Seq(formatString.format("id", "description"))
        val sepa = Seq(line(lenId + 1) + "+" + line(lenDesc + 1))
        val lines = selectables.map(e => formatString.format(e.id, e.description))
        (sepa ++ header ++ sepa ++ lines).mkString("\n")
      } else {
        val formatString = s"%-${lenId}s : %-${lenDesc}s"
        val lines = selectables.map(e => formatString.format(e.id, e.description))
        lines.mkString("\n")
      }
    }


    val builder = OParser.builder[Command]
    val parser = {
      import builder.*
      OParser.sequence(
        programName("pi-hilbert"),
        head("Represent pi as a hilbert curve"),
        cmd("tryout")
          .text("To be used during development. Call a function in Tryout")
          .action((_, _) => TryoutCommand
          ),
        cmd("hp")
          .text("Create the 'pi' homepage")
          .action((_, _) => HpCommand
          ),
        cmd("server")
          .text("Starts the web control panal on port 8080")
          .action((_, _) => ServerCommand
          ),
        cmd("draw")
          .text("Draw a hilbert curve")
          .action((_, c) => DrawCommand(id = "", tiles = false, thumbnails = false))
          .children(
            opt[Unit]('t', "tiles")
              .text("Creates tiles from the drawing")
              .action((_, c) => {
                c.asInstanceOf[DrawCommand].copy(tiles = true)
              }),
            opt[Unit]("thumbnails")
              .text("Creates thumbnails")
              .action((_, c) => {
                c.asInstanceOf[DrawCommand].copy(thumbnails = true)
              }),
            opt[String]("id")
              .required()
              .text(s"Draws a configuratin for a hilbert curve. Valid configurations are:\n${table(cfgs, true)}")
              .validate { x =>
                val ids = cfgs.map(_.id)
                if ids.contains(x) then success
                else {
                  val idsStr = ids.mkString(", ")
                  failure(s"id must be one of: $idsStr")
                }
              }
              .action((x, c) => {
                c.asInstanceOf[DrawCommand].copy(id = x)
              }),
          ),
        help('h', "help").text("prints this usage text"),
      )
    }

    OParser.parse(parser, args, TryoutCommand) match {
      case Some(config) => config
      case _ => ErrorCommand
    }
  }

  def main(args: Array[String]): Unit = {

    val command: Command = parse(args)

    command match {
      case DrawCommand(id, tiles, thumbnails) =>
        val cfg = cfgs.filter(_.id == id).head
        if tiles then Tiles.run(cfg)
        else Drawing.run(cfg, thumbnails)
      case TryoutCommand => Tryout.doIt()
      case HpCommand => Hp.create()
      case ServerCommand => {
        CaskServer().run()
      }
      case ErrorCommand => // Nothing to do
    }
  }
}
