package pi

import pi.Main.{Canvas, Line, PiConfig, Point}

import java.awt.{BasicStroke, Color, Graphics2D}
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import scala.util.Random


object Drawing {

  class CanvasAwt(val id: String, val width: Int, val height: Int, stroke: Double, background: Color) extends Canvas {

    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR)
    val graphics = image.getGraphics.asInstanceOf[Graphics2D]
    graphics.setColor(background)
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

  def run(cfg: PiConfig): Unit = {
    val canvas: Canvas = CanvasAwt(cfg.id, cfg.width, cfg.width, cfg.stroke, cfg.background)
    HilbertTurtle.draw(cfg.depth, canvas, cfg.colors)
    canvas.close()
  }

}
