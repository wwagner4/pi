package pi

import pi.Main.{Line, Point, Canvas}

import java.awt.{BasicStroke, Color, Graphics2D}
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import scala.util.Random



object Drawing {

  val stroke = 3.0F

  class CanvasAwt(val width: Int, val height: Int) extends Canvas {

    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR)
    val graphics = image.getGraphics.asInstanceOf[Graphics2D]
    graphics.setColor(Color.WHITE)
    graphics.fillRect(0, 0, width, height)
    graphics.setStroke(new BasicStroke(stroke))


    def line(color: Color, from: Point, to: Point): Unit = {
      graphics.setColor(color)
      graphics.drawLine(from.x.toInt, height - from.y.toInt, to.x.toInt, height - to.y.toInt)
    }

    def close(): Unit = {
    val outFile = Path.of("target/a.png").toFile
    ImageIO.write(image, "png", outFile)
    println(s"wrote image to ${outFile.getAbsolutePath}")
    }


  }

  def run(): Unit = {
    val canvas: Canvas = CanvasAwt(3000, 3000)
    val colors = ColorIterator.random
    HilbertTurtle.draw(8, canvas, colors)
    canvas.close()
  }

}
