package pi

import pi.Main.{Canvas, Line, Point}

import java.awt.Color
import scala.util.Random

object Hilbert {

  enum Orientation:
    case Up, Down, Left, Right

  enum Direction :
    case Clockwise, CounterClockwise

  case class HilbertDefinition(orientation: Orientation, direction: Direction)

  val colors = Seq(Color.BLACK, Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED, Color.CYAN)

  def ranColor: Color = {
    val i = Random.nextInt(colors.length)
    colors(i)
  }

  def draw(level: Int, canvas: Canvas): Seq[Line] = {

    val color = ranColor

    def connections(level: Int, origin: Point, side: Double, definition: HilbertDefinition): (Seq[Line], Seq[Line], Seq[Line]) = {
      if level <= 0 then (Seq.empty[Line], Seq.empty[Line], Seq.empty[Line])
      else definition match {
        case HilbertDefinition(Orientation.Up, Direction.Clockwise) => 
          ???
        case HilbertDefinition(Orientation.Down, Direction.Clockwise) => 
          ???
        case HilbertDefinition(Orientation.Right, Direction.Clockwise) => 
          ???
        case HilbertDefinition(Orientation.Left, Direction.Clockwise) => 
          ???
        case HilbertDefinition(Orientation.Up, Direction.CounterClockwise) => 
          ???
        case HilbertDefinition(Orientation.Down, Direction.CounterClockwise) => 
          ???
        case HilbertDefinition(Orientation.Right, Direction.CounterClockwise) => 
          ???
        case HilbertDefinition(Orientation.Left, Direction.CounterClockwise) => 
          ???
      }
    }

    def sides(level: Int, origin: Point, side: Double, definition: HilbertDefinition): (Seq[Line], Seq[Line], Seq[Line], Seq[Line]) = {
      if level <= 1 then (Seq.empty[Line], Seq.empty[Line], Seq.empty[Line], Seq.empty[Line])
      else definition match {
        case HilbertDefinition(Orientation.Up, Direction.Clockwise) => 
          ???
        case HilbertDefinition(Orientation.Down, Direction.Clockwise) => 
          ???
        case HilbertDefinition(Orientation.Right, Direction.Clockwise) => 
          ???
        case HilbertDefinition(Orientation.Left, Direction.Clockwise) => 
          ???
        case HilbertDefinition(Orientation.Up, Direction.CounterClockwise) => 
          ???
        case HilbertDefinition(Orientation.Down, Direction.CounterClockwise) => 
          ???
        case HilbertDefinition(Orientation.Right, Direction.CounterClockwise) => 
          ???
        case HilbertDefinition(Orientation.Left, Direction.CounterClockwise) => 
          ???
      }
    }

    def drawRecursive(level: Int, origin: Point, side: Double, definition: HilbertDefinition): Seq[Line] = {
      val (c1, c2, c3) = connections(level - 1, origin, side, definition)
      val (s1, s2, s3, s4) = sides(level - 1, origin, side, definition)
      s1 ++ c1 ++ s2 ++ c2 ++ s3 ++ c3 ++ s4
    }

    val side = math.min(canvas.width, canvas.height).toDouble
    val origin = Point(0, 0)
    val startDefinition = HilbertDefinition(Orientation.Up, Direction.Clockwise)
    drawRecursive(level, origin, side, startDefinition)


  }

}
