package pi

import org.imgscalr.Scalr
import pi.Main.{Canvas, DrawConfig, Point}

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D, Image}
import java.nio.file.{Files, Path}
import javax.imageio.ImageIO
import scala.io.Source
import scala.util.Random

object Tryout {

  def doIt(): Unit = createMapImages()

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
      colors = ColorIterator.pi(ColorIterator.seqColorHue)
    )
    val canvas = Drawing.CanvasAwt(cfg)
    HilbertTurtle.draw(hilbertDepth, canvas, cfg.colors)

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
      val colors = ColorIterator.pi(ColorIterator.seqColorBright3)
      val cfg = DrawConfig(s"reso-$depth-$size", depth, size, 1, Color.BLACK, colors)
      Drawing.run(cfg)
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
      HilbertTurtle.draw(depth, c, ColorIterator.red)
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
