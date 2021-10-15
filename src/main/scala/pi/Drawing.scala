package pi

import org.imgscalr.Scalr
import pi.Config.{Canvas, DrawConfig, Point}

import java.awt.{BasicStroke, Color, Graphics2D}
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.{Files, Path}
import javax.imageio.ImageIO
import scala.util.Random


object Drawing {

  class CanvasAwt(cfg: DrawConfig) extends Canvas {

    override def size: Int = cfg.size

    val image = new BufferedImage(cfg.size, cfg.size, BufferedImage.TYPE_INT_BGR)
    private val graphics = image.getGraphics.asInstanceOf[Graphics2D]
    graphics.setColor(cfg.background)
    graphics.fillRect(0, 0, cfg.size, cfg.size)
    graphics.setStroke(new BasicStroke(cfg.stroke.toFloat))

    def line(color: Color, from: Point, to: Point): Unit = {
      graphics.setColor(color)
      graphics.drawLine(
        math.floor(from.x).toInt, cfg.size - math.ceil(from.y).toInt,
        math.floor(to.x).toInt, cfg.size - math.ceil(to.y).toInt)
    }

    def close(): Unit = {
      // Nothing to do here
    }
  }

  class CanvasAwtFile(cfg: DrawConfig, createThumbnails: Boolean) extends CanvasAwt(cfg) {


    override def close(): Unit = {

      val outFile = Util.drawingFile(cfg.id).toFile
      ImageIO.write(image, "png", outFile)
      println(s"Wrote image to ${outFile.getAbsolutePath}")

      if createThumbnails then {
        val thumb = Scalr.resize(image, Scalr.Method.BALANCED, 300, 300);
        val thumbFile = Util.drawingThumbFile(cfg.id).toFile
        ImageIO.write(thumb, "png", thumbFile)
        println(s"Wrote thumbnail image to ${thumbFile.getAbsolutePath}")
      }
    }

  }

  def run(cfg: DrawConfig, createThumbnails: Boolean): Unit = {
    val canvas: Canvas = CanvasAwtFile(cfg, createThumbnails)
    HilbertTurtle.draw(cfg.depth, canvas, cfg.colors())
    canvas.close()
  }

}
