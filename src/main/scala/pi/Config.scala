package pi

import java.awt.Color
import pi.Util as PiUtil

object Config {
  case class Point(
                    x: Double,
                    y: Double
                  ) {
    def add(dx: Double, dy: Double): Point = Point(x + dx, y + dy)
  }

  trait Canvas {
    def size: Int

    def line(color: Color, from: Point, to: Point): Unit

    def close(): Unit
  }

  trait IdDescriptionPair {
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
                       ) extends IdDescriptionPair


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
}
