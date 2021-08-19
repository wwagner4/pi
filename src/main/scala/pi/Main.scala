package pi

import java.awt.Color

object Main {

  case class Point(x: Double, y:Double) {

    def add(dx: Double, dy: Double): Point = Point(x + dx, y + dy)

  }

  case class Line(color: Color, start: Point, end:Point)

  trait Canvas {
    def width: Int
    def height: Int
    def line(color: Color, from: Point, to: Point): Unit
    def close(): Unit
  }

  def main(args: Array[String]): Unit ={
    Drawing.run()
  }

}
