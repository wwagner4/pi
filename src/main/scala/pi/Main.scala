package pi

import java.awt.Color

object Main {

  case class Point(x: Double, y:Double) {

    def add(dx: Double, dy: Double): Point = Point(x + dx, y + dy)

  }

  case class Line(color: Color, start: Point, end:Point)

  case class Canvas(width: Int, height: Int)

  def main(args: Array[String]): Unit ={
    Drawing.run()
  }

}
