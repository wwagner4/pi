package pi

import java.awt.Color

object Main {

  case class Point(x: Double, y:Double)

  case class Line(color: Color, start: Point, end:Point)

  case class Canvas(width: Int, height: Int)

  def main(args: Array[String]): Unit ={
    Drawing.run()
  }

}
