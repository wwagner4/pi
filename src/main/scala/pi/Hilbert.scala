package pi

import pi.Main.{Canvas, Line, Point}

import java.awt.Color
import scala.util.Random

object Hilbert {

  val colors = Seq(Color.BLACK, Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED, Color.CYAN)

  def ranColor: Color = {
    val i = Random.nextInt(colors.length)
    colors(i)
  }

  def draw(level: Int, canvas: Canvas): Seq[Line] = ???

}
