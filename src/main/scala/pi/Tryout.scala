package pi

import pi.Main.{Canvas, Point}

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D}
import java.nio.file.Path
import javax.imageio.ImageIO
import scala.util.Random

object Tryout {

  def curveLength(): Unit = {

    class CanvasCount() extends Canvas {
      var count = 0

      override def height: Int = 0

      override def width: Int = 0

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
