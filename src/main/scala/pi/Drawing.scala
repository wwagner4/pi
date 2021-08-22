package pi

import org.imgscalr.Scalr
import pi.Main.{Canvas, Line, PiConfig, Point}

import java.awt.{BasicStroke, Color, Graphics2D}
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import scala.util.Random


object Drawing {

  class CanvasAwt(cfg: PiConfig) extends Canvas {
    
    def size = cfg.width
    def height = cfg.width

    val image = new BufferedImage(size, height, BufferedImage.TYPE_INT_BGR)
    val graphics = image.getGraphics.asInstanceOf[Graphics2D]
    graphics.setColor(cfg.background)
    graphics.fillRect(0, 0, size, height)
    graphics.setStroke(new BasicStroke(cfg.stroke.toFloat))


    def line(color: Color, from: Point, to: Point): Unit = {
      graphics.setColor(color)
      graphics.drawLine(from.x.toInt, height - from.y.toInt, to.x.toInt, height - to.y.toInt)
    }

    def close(): Unit = {
      val outFile = Path.of(s"target/${cfg.id}.png").toFile
      ImageIO.write(image, "png", outFile)
      println(s"Wrote image to ${outFile.getAbsolutePath}")

      if cfg.createThumbnail then {
        val thumb = Scalr.resize(image, Scalr.Method.BALANCED, 300, 300);
        val thumbFile = Path.of(s"target/${cfg.id}-thumb.png").toFile
        ImageIO.write(thumb, "png", thumbFile)
        println(s"Wrote thumbnail image to ${thumbFile.getAbsolutePath}")
      }
    }


  }

  def run(cfg: PiConfig): Unit = {
    val canvas: Canvas = CanvasAwt(cfg)
    HilbertTurtle.draw(cfg.depth, canvas, cfg.colors)
    canvas.close()
  }

}
