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

  def draw(level: Int, canvas: Canvas): Seq[Line] = {

    val color = Color.BLACK

    def connectDown(level: Int, origin: Point, side: Double) = {
      val nextSide = side / 2.0
      val p1 = origin.add(3 * nextSide, 3 * nextSide) 
      val p2 = origin.add(3 * nextSide, nextSide) 
      Seq(Line(color, p1, p2))
    }

    def connectUp(level: Int, origin: Point, side: Double): Seq[Line] = {
      val nextSide = side / 2.0
      val p1 = origin.add(nextSide, nextSide) 
      val p2 = origin.add(nextSide, 3 * nextSide) 
      Seq(Line(color, p1, p2))
    }

    def connectRight(level: Int, origin: Point, side: Double) = {
      val nextSide = side / 2.0
      val p1 = origin.add(nextSide, nextSide) 
      val p2 = origin.add(3 * nextSide, nextSide) 
      Seq(Line(color, p1, p2))
    }

    def connectLeft(level: Int, origin: Point, side: Double) = {
      ???
    }

    def drawUp(level: Int, origin: Point, side: Double): Seq[Line] = {
      if level <= 0 then Seq.empty[Line]
      else {
        val nextSide = side / 2.0

        val c1 = connectUp(level, origin, nextSide)
        val c2 = connectRight(level, origin, nextSide)
        val c3 = connectDown(level, origin, nextSide)

        val d1 = drawRight(level - 1, origin, nextSide)
        val d2 = drawUp(level-1, origin.copy(y= origin.y + nextSide), nextSide)
        val d3 = drawUp(level-1, origin.copy(y= origin.y + nextSide, x = origin.x + nextSide), nextSide)
        val d4 = drawLeft(level - 1, origin.copy(x = origin.x + nextSide), nextSide)

        d1 ++ c1 ++ d2 ++ c2 ++ d3 ++ c3 ++ d4
      }
    }

    def drawDown(level: Int, origin: Point, side: Double): Seq[Line] = {
      if level <= 0 then Seq.empty[Line]
      else {
        val nextSide = side / 2.0
        ???
      }
    }

    def drawRight(level: Int, origin: Point, side: Double): Seq[Line] = {
      if level <= 0 then Seq.empty[Line]
      else {
        val nextSide = side / 2.0
        ???
      }
    }

    def drawLeft(level: Int, origin: Point, side: Double): Seq[Line] = {
      if level <= 0 then Seq.empty[Line]
      else {
        val nextSide = side / 2.0
        ???
      }
    }

    val side = math.min(canvas.width, canvas.height).toDouble
    val origin = Point(0, 0)
    drawUp(level, origin, side)


  }

}
