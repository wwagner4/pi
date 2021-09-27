package pi

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import pi.Main.*

import java.awt.Color
import javax.imageio.ImageIO
import org.imgscalr.Scalr


object Hp {

  trait RebuildDef

  object RebuildDef {

    case object None extends RebuildDef

    case object All extends RebuildDef

    case class Some(ids: Seq[String]) extends RebuildDef

  }

  case class Image(
                    id: String,
                    text: String,
                    hilbertDepth: Int,
                    initialResolution: Int,
                    prescaling: Int,
                    zoomLevels: String,
                    colors: Colors,
                    backgroundColor: Color = Color.BLACK,
                  )

  case class Group(text: String, images: Seq[Image])

  case class Page(title: String, groups: Seq[Group], imagesForRebuild: RebuildDef)

  private val page = Page(
    title = "Idea of this project is to visualize &pi; by means of a Hilbert polygon",
    imagesForRebuild = RebuildDef.All,
    groups = Seq(
      Group(
        text = "Every digit between 0 and 9 is encoded by a color",
        images = Seq(
          Image(
            id = "pi",
            text = "262143 digits of &pi; on a Hilbert curve",
            hilbertDepth = 9,
            prescaling = 16,
            initialResolution = 8,
            zoomLevels = "1-6",
            colors = Colors.Pi(DigiMap.Hue),
          ),
          Image(
            id = "ran",
            text = "262143 random digits on a Hilbert curve",
            hilbertDepth = 9,
            prescaling = 16,
            initialResolution = 8,
            zoomLevels = "1-6",
            colors = Colors.Random(DigiMap.Hue),
          ),
        )
      ),
    )
  )

  def images(images: Seq[Image]): String = {
    def img(i: Image) =
      s"""<div class="image-out">
         |    <div class="image">
         |        <div class="image-text">${i.text}</div>
         |        <div id="${i.id}" class="map"></div>
         |    </div>
         |</div>
         |""".stripMargin

    images.map(i => img(i)).mkString("\n")
  }

  def groups(groups: Seq[Group]): String = {
    def grp(g: Group) =
      s"""<div class="text">${g.text}</div>
         |${images(g.images)}
         |""".stripMargin

    groups.map(gr => grp(gr)).mkString("\n")
  }

  def index(olJs: Seq[String]): String = {
    s"""<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
       |<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
       |<head>
       |    <title>pi</title>
       |    <meta http-equiv='imagetoolbar' content='no'/>
       |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
       |
       |    <style>
       |      @import url('https://fonts.googleapis.com/css2?family=Jura:wght@600&display=swap');
       |    </style>
       |
       |    <style>
       |        v\\: * {
       |            behavior: url(#default#VML);
       |        }
       |        html, body {
       |            padding: 0;
       |            font-family: 'Jura', sans-serif;
       |            color: white;
       |        }
       |        body {
       |            background: #a7ab93;
       |        }
       |        .content {
       |            max-width: 60em;
       |            margin: 6em auto 19em;
       |        }
       |        .title {
       |            text-align: center;
       |            padding: 0 0 150px 0;
       |            font-size: 250px;
       |        }
       |        .header {
       |            text-align: center;
       |            padding: 0 0 0 0;
       |            font-size: 40px;
       |        }
       |        .text {
       |            font-size: 1.3em;
       |            padding: 3em 0 1em 0;
       |        }
       |
       |        .image-out {
       |            border: 1px solid white;
       |            margin: 0 0 1em 0;
       |        }
       |
       |        .image {
       |            margin: 1em;
       |        }
       |
       |
       |        .map {
       |            width: 100%;
       |            aspect-ratio: 1;
       |        }
       |
       |        .image-text {
       |            font-size: 1.3em;
       |            padding: 0 0 1em 0;
       |        }
       |    </style>
       |    <link rel="stylesheet"
       |          href="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/css/ol.css"
       |          type="text/css">
       |    <script src="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.3.1/build/ol.js"></script>
       |    <script src="https://unpkg.com/ol-layerswitcher@3.5.0"></script>
       |    <link rel="stylesheet" href="https://unpkg.com/ol-layerswitcher@3.5.0/src/ol-layerswitcher.css"/>
       |</head>
       |<body>
       |<div class="content">
       |    <div class="title">&pi;</div>
       |    <div class="header">Idea of this project is to visualize &pi; by means of a Hilbert polygon</div>
       |    ${groups(page.groups)}
       |</div>
       |<script type="text/javascript">
       |${olJs.mkString("\n")}
       |</script>
       |</body>
       |</html>
       |
       |""".stripMargin
  }

  def extractOpenLayerJs1(html: String, img: Image): String = {
    val beginIndex = html.indexOf("new ol.Map")
    val stopStr = "});"
    val endIndex = html.lastIndexOf(stopStr) + stopStr.length
    val base = html.substring(beginIndex, endIndex)
    val resolutionRegex = "resolution:.*,".r
    val replaced = base
      .replace("return ('./", s"return ('${img.id}/")
      .replace("target: 'map'", s"target: '${img.id}'")
      .replace("extend([mousePositionControl])", s"extend([new ol.control.FullScreen()])")
    resolutionRegex.replaceFirstIn(replaced, s"resolution: ${img.initialResolution},")
  }

  def create(): Unit = {
    val outPath = Util.piWorkPath.resolve("hp")
    if Files.notExists(outPath) then Files.createDirectories(outPath)
    println(s"Creating Homepage in ${outPath.toAbsolutePath}")

    val imagePath = Util.piWorkPath.resolve("hp-image")
    if Files.notExists(imagePath) then Files.createDirectories(imagePath)

    val olJsList = page.groups.flatMap(g => g.images).map { hpImg =>
      println(s"Creating $hpImg")
      val size = Util.minCanvasSizeForDepth(hpImg.hilbertDepth)

      val tilesPath = outPath.resolve(hpImg.id)
      page.imagesForRebuild match {
        case RebuildDef.All =>
          rebuildTiles(hpImg, size, tilesPath, imagePath)
        case RebuildDef.Some(ids) =>
          if ids.contains(hpImg.id) then rebuildTiles(hpImg, size, tilesPath, imagePath)
        case RebuildDef.None => // Nothing to do
      }

      val htmlFile = tilesPath.resolve("openlayers.html")
      val src = scala.io.Source.fromFile(htmlFile.toFile)
      val htmlStr = try src.getLines().mkString("\n") finally src.close()
      extractOpenLayerJs1(htmlStr, hpImg)
    }

    val indexPath = outPath.resolve("index.html")
    Files.write(indexPath, index(olJsList).trim.getBytes(StandardCharsets.UTF_8))
    println(s"Wrote html to $indexPath")
  }

  private def rebuildTiles(hpImg: _root_.pi.Hp.Image, size: Int, tilesPath: Path, imagePath: Path): Unit = {
    val cfg = DrawConfig(hpImg.id, hpImg.hilbertDepth, size, 1,
      hpImg.backgroundColor,
      ColorIterator.iterator(hpImg.colors))

    val canvas = Drawing.CanvasAwt(cfg)
    HilbertTurtle.draw(cfg.depth, canvas, cfg.colors)
    val img = canvas.image

    val sizeScaled = size * hpImg.prescaling
    val imgScaled = Scalr.resize(img, Scalr.Method.SPEED, sizeScaled, sizeScaled)

    val imgFile = imagePath.resolve(s"${hpImg.id}.png")
    ImageIO.write(imgScaled, "png", imgFile.toFile)
    println(s"Wrote image of size $sizeScaled to $imgFile")
    EasyExec.runAllCommands(Seq(
      Seq("gdal2tiles.py", "-p", "raster", "-r", "near", "-z", hpImg.zoomLevels,
        imgFile.toAbsolutePath.toString, tilesPath.toAbsolutePath.toString)))
  }
}
