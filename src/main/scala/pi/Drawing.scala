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
      ???
    }

    def close(): Unit = {
      ???
    }


  }

  def run(): Unit = {
    val canvas: Canvas = CanvasAwt(3000, 3000)
    HilbertTurtle.draw(8, canvas)
  }




  private def drawImage(canvas: Canvas, poligone: Seq[Line]) = {
    val image = new BufferedImage(canvas.width, canvas.height, BufferedImage.TYPE_INT_BGR)
    val graphics = image.getGraphics.asInstanceOf[Graphics2D]
    paint(graphics, canvas, poligone)
    val outFile = Path.of("target/a.png").toFile
    ImageIO.write(image, "png", outFile)
    println(s"wrote image to ${outFile.getAbsolutePath}")
  }

  private def paint(g: Graphics2D, canvas: Canvas, poli: Seq[Line]) = {
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, canvas.width, canvas.height)
    g.setStroke(new BasicStroke(stroke))


    val h = canvas.height
    poli.foreach { l =>
      g.setColor(l.color)
      g.drawLine(l.start.x.toInt, h - l.start.y.toInt, l.end.x.toInt, h - l.end.y.toInt)
    }
  }
}
