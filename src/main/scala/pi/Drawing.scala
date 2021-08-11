package pi

import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import scala.util.Random

case class Canvas(width: Int, height: Int)


class Drawing {

  def run(): Unit = {
    val c = Canvas(5000, 4000)
    val bi = new BufferedImage(c.width, c.height, BufferedImage.TYPE_INT_BGR)
    val g = bi.getGraphics.asInstanceOf[Graphics2D]
    paint(g, c)
    val outFile = Path.of("target/a.png").toFile
    ImageIO.write(bi, "png", outFile)
    println(s"wrote image to ${outFile.getAbsolutePath}")
  }

  private def paint(g: Graphics2D, canvas: Canvas) = {
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, canvas.width, canvas.height)

    for (i <- 0 to 1000) {
      val fromX = Random.nextInt(canvas.width)
      val toX = Random.nextInt(canvas.width)
      val fromY = Random.nextInt(canvas.height)
      val toY = Random.nextInt(canvas.height)
      g.setColor(Color.BLACK)
      g.drawLine(fromX, toX, fromY, toY)
    }
  }
}
