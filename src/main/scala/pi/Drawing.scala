package pi

import pi.Main.{Line, Point, Canvas}

import java.awt.{BasicStroke, Color, Graphics2D}
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import scala.util.Random



object Drawing {

  val stroke = 10.0F

  def run(): Unit = {
    val canvas = Canvas(5000, 5000)
    val poligone = RandomWalk.draw(canvas)
    drawImage(canvas, poligone)
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

    poli.foreach { l =>
      g.setColor(l.color)
      g.drawLine(l.start.x.toInt, l.start.y.toInt, l.end.x.toInt, l.end.y.toInt)
    }
  }
}