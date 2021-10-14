package pi

import Main.{DrawConfig, TilesConfig}
import org.imgscalr.Scalr
import pi.Util

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import javax.imageio.ImageIO
import scala.io.BufferedSource
import scala.xml.XML

object Tiles {

  case class TileMapResource(
                              size: Int,
                              resolutions: Seq[Int],
                            )

  def tileMapResource(id: String): TileMapResource = {
    def extractSize(id: String): Int = {
      val mapResourceXml = XML.loadFile(Util.tileMapResourceFile(id).toFile)
      (mapResourceXml \ "BoundingBox" \@ "maxx").toDouble.toInt
    }

    def extractResolutions(id: String): Seq[Int] = {
      val htmlFile = Util.openLayersHtmlFile(id)
      val html = scala.io.Source.fromFile(htmlFile.toFile).mkString
      """resolutions: \[(.*)\],""".r
        .findAllIn(html)
        .matchData
        .toSeq
        .flatMap(m => (m.group(1)).split(","))
        .map(_.toInt)
        .sorted
        .reverse
    }

    TileMapResource(size = extractSize(id), resolutions = extractResolutions(id))
  }

  def rebuildTiles(drawConfig: DrawConfig, tilesPath: Path, drawingFile: Path): Unit = {

    println("Creating hilbert curve")
    Drawing.run(drawConfig, false)
    val img = ImageIO.read(drawingFile.toFile)

    val sizeScaled = drawConfig.size * drawConfig.tilesConfig.prescaling
    println(s"Scaling $sizeScaled")
    val imgScaled = Scalr.resize(img, Scalr.Method.SPEED, sizeScaled, sizeScaled)

    val drawingFileScaled = drawingFile.getParent().resolve(s"scaled-${drawConfig.id}.png")
    ImageIO.write(imgScaled, "png", drawingFileScaled.toFile)
    println(s"Wrote image of size $sizeScaled to $drawingFileScaled")
    EasyExec.runAllCommands(Seq(
      Seq("gdal2tiles.py", "-p", "raster", "-r", "near", "-z", drawConfig.tilesConfig.zoomLevels,
        drawingFileScaled.toAbsolutePath.toString, tilesPath.toAbsolutePath.toString)))
    println(s"Created tiles in $tilesPath")
  }

  def run(drawConfig: DrawConfig): Unit = {
    println("Tiles run")
    val tilesPath = {
      val tilesBasePath = Util.piWorkPath.resolve("tiles")
      if Files.notExists(tilesBasePath) then Files.createDirectories(tilesBasePath)
      tilesBasePath.resolve(drawConfig.id)
    }
    if Files.notExists(tilesPath) then Files.createDirectories(tilesPath)
    val drawingFile = Util.drawingFile(drawConfig.id)
    rebuildTiles(drawConfig, tilesPath, drawingFile)
    createHtml(drawConfig, tilesPath)
  }

  def createHtml(drawConfig: DrawConfig, tilesPath: Path): Unit = {
    Util.copyResource("sample11b.png", "background.png", tilesPath)
    Util.copyResource("FrederickatheGreat-Regular.ttf", "FrederickatheGreat-Regular.ttf", tilesPath)
    val tmr = tileMapResource(drawConfig.id)

    val initialResolutionStr = "%.4f".format(drawConfig.tilesConfig.initialResolution)
    val sizeHalfStr = (tmr.size / 2).toString
    val sizeStr = tmr.size.toString
    val resolutionsStr = tmr.resolutions.mkString(", ")
    val rightmarginStr = "%.2f".format(drawConfig.tilesConfig.rightMarginEm)
    val pageStr =
      s"""
         |<!DOCTYPE html>
         |<html lang="en">
         |<head>
         |    <meta charset="UTF-8">
         |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
         |    <title>hilbert ${drawConfig.id}</title>
         |    <style>
         |        @font-face {
         |            font-family: 'pifont';
         |            src: url('FrederickatheGreat-Regular.ttf')  format('truetype');
         |        }
         |        body {
         |            background-color: #d9d579;
         |            font-family: 'pifont', sans-serif;
         |            background-image: url("background.png");
         |        }
         |
         |        .content {
         |            background-color: #00000000;
         |            max-width: 60em;
         |            margin: 0 auto 0 auto;
         |        }
         |
         |        .hilb-cont {
         |            margin: 0 0 0.4em 0;
         |            background-color: #00000000;
         |        }
         |
         |        .hilb {
         |            margin-right: ${rightmarginStr}em;
         |            background-color: rgba(255, 255, 255, 0.185);
         |            aspect-ratio: 1;
         |        }
         |        .head {
         |            font-size: 1.5em;
         |            background-color: #00000000;
         |            padding-bottom: 1em;
         |            padding-left: 0.3em;
         |            padding-right: 0.3em;
         |        }
         |        .title {
         |            font-size: 3em;
         |            background-color: #00000000;
         |            text-align: center;
         |            padding: 2em 0.3em 1em;
         |        }
         |
         |    </style>
         |    <link rel="stylesheet"
         |        href="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/css/ol.css"
         |        type="text/css"/>
         |    <link rel="stylesheet"
         |        href="https://unpkg.com/ol-layerswitcher@3.5.0/src/ol-layerswitcher.css"
         |        type="text/css"/>
         |    <script src="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/build/ol.js"></script>
         |    <script src="https://unpkg.com/ol-layerswitcher@3.5.0"></script>
         |</head>
         |<body>
         |<div class="content">
         |    <div class="title">
         |        HILBERT CURVES
         |    </div>
         |    <div class="head">
         |        A hilbert curve is a space filling curve.
         |        These are curves whose range contain
         |        an entire two-dimensional object.
         |        They were first described by the german mathematician
         |        David Hilbert in 1891
         |    </div>
         |    <div class="head">
         |        ${drawConfig.id}
         |    </div>
         |    <div class="head">
         |        ${drawConfig.description}
         |    </div>
         |    <div class="hilb-cont">
         |        <div id="hilb-ol" class="hilb"></div>
         |    </div>
         |</div>
         |<script type="text/javascript">
         |    new ol.Map({
         |                controls: ol.control.defaults().extend([new ol.control.FullScreen()]),
         |                target: 'hilb-ol',
         |
         |                layers: [
         |                    new ol.layer.Group({
         |                        title: 'Overlay',
         |                        layers: [
         |                            new ol.layer.Tile({
         |                                title: 'Overlay',
         |                                // opacity: 0.7,
         |                                source: new ol.source.TileImage({
         |                                    attributions: '',
         |                                    tileGrid: new ol.tilegrid.TileGrid({
         |                                        extent: [0,-$sizeStr,$sizeStr,0],
         |                                        origin: [0,-$sizeStr],
         |                                        resolutions: [$resolutionsStr],
         |                                        tileSize: [256, 256]
         |                                    }),
         |                                    tileUrlFunction: function(tileCoord) {
         |                                        return ('./{z}/{x}/{y}.png'
         |                                            .replace('{z}', String(tileCoord[0]))
         |                                            .replace('{x}', String(tileCoord[1]))
         |                                            .replace('{y}', String(- 1 - tileCoord[2])));
         |                                    },
         |                                })
         |                            }),
         |                        ]
         |                    }),
         |                ],
         |                view: new ol.View({
         |                    center: [$sizeHalfStr, -$sizeHalfStr],
         |                    resolution: $initialResolutionStr,
         |                })
         |            });
         |</script>
         |</body>
         |</html>
         |""".stripMargin

    val htmlFile = tilesPath.resolve("index.html")
    Files.write(htmlFile, pageStr.getBytes(StandardCharsets.UTF_8))
    println(s"Wrote html to $htmlFile")
  }
}