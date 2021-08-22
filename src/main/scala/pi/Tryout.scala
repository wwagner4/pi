package pi

import pi.Main.{Canvas, DrawConfig, Point}

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D}
import java.nio.file.{Files, Path}
import javax.imageio.ImageIO
import scala.io.Source
import scala.util.Random

object Tryout {

  def doIt(): Unit = drawMinimumSize()
  
  def drawMinimumSize(): Unit = {
    Seq(9, 10, 11, 12).foreach { depth =>
      val size = Util.minCanvasSizeForDepth(depth)
      val colors = ColorIterator.pi (ColorIterator.seqColorBright3)
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
