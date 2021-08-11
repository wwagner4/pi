package pi

import pi.Hilbert.ranColor
import pi.Main.{Canvas, Line, Point}

import scala.util.Random

object RandomWalk {
  def draw(c: Canvas): Seq[Line] = {
    val len = 20

    def randomWalkInternal(canvas: Canvas, line: List[Line]): List[Line] = {
      if line.length >= 10000 then line
      else
        val lastLine = line.head
        val nextLine = Random.nextInt(4) match {
          case 0 => Line(ranColor, lastLine.end, Point(lastLine.end.x + len, lastLine.end.y))
          case 1 => Line(ranColor, lastLine.end, Point(lastLine.end.x, lastLine.end.y + len))
          case 2 => Line(ranColor, lastLine.end, Point(lastLine.end.x - len, lastLine.end.y))
          case 3 => Line(ranColor, lastLine.end, Point(lastLine.end.x, lastLine.end.y - len))
        }
        randomWalkInternal(canvas, nextLine :: line)
    }

    val startPoint = Point(c.width / 2, c.height / 2)
    val startLine = Line(ranColor, startPoint, startPoint)
    randomWalkInternal(c, List(startLine))
  }
}
