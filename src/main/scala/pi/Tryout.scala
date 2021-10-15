package pi

import org.imgscalr.Scalr
import pi.Config.{Canvas, DrawConfig, Point, TilesConfig}

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D, Image}
import java.nio.file.{Files, Path}
import javax.imageio.ImageIO
import javax.sql.rowset.spi.XmlReader
import scala.io.Source
import scala.util.Random
import scala.xml.XML


object Tryout {

  def doIt(): Unit = tileMapRes()

  def tileMapRes(): Unit = {
    println(s"--- TileMapResource: ${Tiles.tileMapResource("cf1")}")
  }

  def extractOpenLayerJs(): Unit = {
    println("Extract map code")

    val openLayerHtml =
      """
        |<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
        |<html xmlns="http://www.w3.org/1999/xhtml">
        |    <head>
        |    <title>map.png</title>
        |    <meta http-equiv='imagetoolbar' content='no'/>
        |    <style type="text/css"> v\:* {behavior:url(#default#VML);}
        |        html, body { overflow: hidden; padding: 0; height: 100%; width: 100%; font-family: 'Lucida Grande',Geneva,Arial,Verdana,sans-serif; }
        |        body { margin: 10px; background: #fff; }
        |        h1 { margin: 0; padding: 6px; border:0; font-size: 20pt; }
        |        #header { height: 43px; padding: 0; background-color: #eee; border: 1px solid #888; }
        |        #subheader { height: 12px; text-align: right; font-size: 10px; color: #555;}
        |        #map { height: 90%; border: 1px solid #888; }
        |    </style>
        |    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/css/ol.css" type="text/css">
        |    <script src="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/build/ol.js"></script>
        |    <script src="https://unpkg.com/ol-layerswitcher@3.5.0"></script>
        |    <link rel="stylesheet" href="https://unpkg.com/ol-layerswitcher@3.5.0/src/ol-layerswitcher.css" />
        |</head>
        |<body>
        |    <div id="header"><h1>map.png</h1></div>
        |    <div id="subheader">Generated by <a href="https://gdal.org/programs/gdal2tiles.html">GDAL2Tiles</a>&nbsp;&nbsp;&nbsp;&nbsp;</div>
        |    <div id="map" class="map"></div>
        |    <div id="mouse-position"></div>
        |    <script type="text/javascript">
        |        var mousePositionControl = new ol.control.MousePosition({
        |            className: 'custom-mouse-position',
        |            target: document.getElementById('mouse-position'),
        |            undefinedHTML: '&nbsp;'
        |        });
        |        var map = new ol.Map({
        |            controls: ol.control.defaults().extend([mousePositionControl]),
        |            target: 'map',
        |
        |            layers: [
        |                new ol.layer.Group({
        |                    title: 'Overlay',
        |                    layers: [
        |                        new ol.layer.Tile({
        |                            title: 'Overlay',
        |                            // opacity: 0.7,
        |                            source: new ol.source.TileImage({
        |                                attributions: '',
        |                                tileGrid: new ol.tilegrid.TileGrid({
        |                                    extent: [0,-2000,2000,0],
        |                                    origin: [0,-2000],
        |                                    resolutions: [8,4,2,1],
        |                                    tileSize: [256, 256]
        |                                }),
        |                                tileUrlFunction: function(tileCoord) {
        |                                    return ('./{z}/{x}/{y}.png'
        |                                        .replace('{z}', String(tileCoord[0]))
        |                                        .replace('{x}', String(tileCoord[1]))
        |                                        .replace('{y}', String(- 1 - tileCoord[2])));
        |                                },
        |                            })
        |                        }),
        |                    ]
        |                }),
        |            ],
        |            view: new ol.View({
        |                center: [1000.000000, -1000.000000],
        |                resolution: 4.000000,
        |            })
        |        });
        |    </script>
        |</body>
        |</html>
        |""".stripMargin.trim


    val cfg = DrawConfig(
      id = "color",
      description = "Colorful Hilbert polygon of depth 9",
      depth = 9,
      size = Util.minCanvasSizeForDepth(9),
      stroke = 1,
      colors = () => ColorIterator.iterator(Colors.Hue(10_000)),
      background = Color.WHITE,
      tilesConfig = TilesConfig(
        prescaling = 32,
        zoomLevels = "1-8",
      ),
    )

    val base = Hp.extractOpenLayerJs1(openLayerHtml, cfg)
    println(base)

  }

  def createMapImages(): Unit = {
    val outpath = Path.of("src", "main", "web")
    if Files.notExists(outpath) then Files.createDirectories(outpath)
    val hilbertDepth = 9

    val baseSize = Util.minCanvasSizeForDepth(hilbertDepth)

    val cfg = DrawConfig(id = s"tile-$hilbertDepth",
      depth = hilbertDepth,
      size = baseSize,
      stroke = 1,
      background = Color.WHITE,
      colors = () => ColorIterator.iterator(Colors.Hue(200))
    )
    val canvas = Drawing.CanvasAwt(cfg)
    HilbertTurtle.draw(hilbertDepth, canvas, cfg.colors())

    def draw(image: BufferedImage, scale: Int): Unit = {
      val size = baseSize * scale
      val scaled = Scalr.resize(image, Scalr.Method.SPEED, size, size);

      val outFile = outpath.resolve(s"${cfg.id}-$scale.png").toFile
      ImageIO.write(scaled, "png", outFile)
      println(s"Wrote image to ${outFile.getAbsolutePath}")
    }

    draw(canvas.image, 8)
  }

  def minimumSizeTable(): Unit = {
    println(f"+-------+----------------+")
    println(f"| depth | resolution[px] |")
    println(f"+-------+----------------+")
    (1 to 15).foreach { depth =>
      val size = Util.minCanvasSizeForDepth(depth)
      println(f"| $depth%5d | $size%14d |")
    }
    println(f"+-------+----------------+")
  }

  def drawMinimumSize(): Unit = {
    Seq(9, 10, 11, 12).foreach { depth =>
      val size = Util.minCanvasSizeForDepth(depth)
      val colors = () => ColorIterator.iterator(Colors.Red)
      val cfg = DrawConfig(s"reso-$depth-$size", depth, size, 1, Color.BLACK, colors)
      Drawing.run(cfg, false)
    }
  }

  def treatmentOfBigConstantsInFiles(): Unit = {
    Util.digitsPiXL
      .zipWithIndex
      .take(100_000_000)
      .foreach { (c, i) =>
        if i % 10_100_000 == 0 then println(s"read another 100,000,000 ${i} $c")
      }
    println("read 100,000,000 numbers")

  }

  def lengthOfHilbertPolygon(): Unit = {

    class CanvasCount() extends Canvas {
      var count = 0

      override def size: Int = 0

      def line(color: Color, from: Point, to: Point): Unit = {
        count += 1
      }

      def close(): Unit = {}
    }

    def len(depth: Int): Int = {
      val c = new CanvasCount()
      HilbertTurtle.draw(depth, c, ColorIterator.iterator(Colors.Red))
      c.count
    }

    def len1(depth: Int): Long = {
      if depth == 1 then 3L
      else 4 * len1(depth - 1) + 3L
    }

    val formatter = java.text.NumberFormat.getIntegerInstance
    val out = (1 to 23)
      .map(d => (d, formatter.format(len1(d))))
      .map((d, l) => f"|$d%8d | $l%20s|")
      .mkString("\n")
    println("Number of lines for hilbert poligon per depth")
    println("+---------+---------------------+")
    println("| depth   | lengh               |")
    println("+---------+---------------------+")
    println(out)
    println("+---------+---------------------+")
  }

}
