package pi

import pi.Tiles.getClass

import java.nio.file.{Files, Path}
import scala.io.{Codec, Source}

object Util {

  def minCanvasSizeForDepth(depth: Int): Int = {
    math.pow(2, depth + 1).toInt + 1
  }

  def linvals[T](n: Int, y1: Double, y2: Double, f: Double => T): Seq[T] = {
    val x1 = 0
    val x2 = n - 1
    val l: Int => Double = i => y1 + (y2 - y1) / (x2 - x1) * i
    (x1 to x2).map(i => f(l(i)))
  }

  def digitsPiXL: Iterator[Int] = {
    val piFile = piWorkPath.resolve("pi.txt")
    require(Files.exists(piFile), s"file must exist $piFile")
    Source.fromFile(piFile.toFile)
      .iterator
      .filter(_.isDigit)
      .map(_.asDigit)
  }

  def digitsPi1mFromResource: Iterator[Int] = {
    Source.fromResource("pi-dec-1m.txt")
      .iterator
      .filter(_.isDigit)
      .map(_.asDigit)
  }

  def piWorkPath: Path = {
    val workPath = if System.getenv("PI_WORK") != null then Path.of(System.getenv("PI_WORK"))
    else Path.of(System.getProperty("user.home"), "work", "pi")
    if Files.notExists(workPath) then Files.createDirectories(workPath)
    workPath
  }

  private def drawingWorkPath: Path = {
    val workPath = piWorkPath.resolve("drawing")
    if Files.notExists(workPath) then Files.createDirectories(workPath)
    workPath
  }

  def drawingFile(id: String): Path = {
    val outFile = drawingWorkPath.resolve(s"$id.png")
    outFile
  }

  def drawingThumbFile(id: String): Path = {
    val outFile = drawingWorkPath.resolve(s"$id.png")
    outFile
  }

  def copyResource(resource: String, fileName: String, outDir: Path): Unit = {
    val is = getClass.getClassLoader.getResourceAsStream(resource)
    val outFile = outDir.resolve(fileName)
    Files.write(outFile, is.readAllBytes())
    println(s"Copied $resource to $outFile")
  }

  def tileMapResourceFile(id: String): Path = {
    val mapResourcePath = Path.of("tiles", id, "tilemapresource.xml")
    Util.piWorkPath.resolve(mapResourcePath)
  }

  def openLayersHtmlFile(id: String): Path = {
    val mapResourcePath = Path.of("tiles", id, "openlayers.html")
    Util.piWorkPath.resolve(mapResourcePath)
  }
}
