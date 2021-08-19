package pi

import pi.Main.{Canvas, Line, PiConfig, Point}

import java.awt.{BasicStroke, Color, Graphics2D}
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import scala.util.Random


object Drawing {

  class CanvasAwt(val id: String, val width: Int, val height: Int, stroke: Double) extends Canvas {

    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR)
    val graphics = image.getGraphics.asInstanceOf[Graphics2D]
    graphics.setColor(Color.WHITE)
    graphics.fillRect(0, 0, width, height)
    graphics.setStroke(new BasicStroke(stroke.toFloat))


    def line(color: Color, from: Point, to: Point): Unit = {
      graphics.setColor(color)
      graphics.drawLine(from.x.toInt, height - from.y.toInt, to.x.toInt, height - to.y.toInt)
    }

    def close(): Unit = {
      val outFile = Path.of(s"target/$id.png").toFile
      ImageIO.write(image, "png", outFile)
      println(s"wrote image to ${outFile.getAbsolutePath}")
    }


  }

  def run(piConfig: PiConfig): Unit = {
    val canvas: Canvas = CanvasAwt(piConfig.id, piConfig.width, piConfig.width, piConfig.stroke)
    HilbertTurtle.draw(piConfig.depth, canvas, piConfig.colors)
    canvas.close()
  }

}
