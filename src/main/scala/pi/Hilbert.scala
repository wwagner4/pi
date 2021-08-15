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

  enum Connection :
    case LeftUp, LeftDown, RightUp, RightDown, TopRight, TopLeft, BottomRight, BottomLeft 


  val colors = Seq(Color.BLACK, Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED, Color.CYAN)

  def ranColor: Color = {
    val i = Random.nextInt(colors.length)
    colors(i)
  }

  def draw(level: Int, canvas: Canvas): Seq[Line] = {

    val col1 = Color.RED
    val col2 = Color.GREEN
    val col3 = Color.BLUE

    def line(color: Color, connection: Connection, level: Int, origin: Point, side: Double): Line = {
      val s1 = side / 4.0
      val s3 = s1 * 3.0
      connection match {
        case Connection.LeftUp => Line(color, origin.add(s1, s1), origin.add(s1, s3)) 
        case Connection.LeftDown => Line(color, origin.add(s1, s3), origin.add(s1, s1))
        case Connection.RightUp => Line(color, origin.add(s3, s1), origin.add(s3, s3))
        case Connection.RightDown => Line(color, origin.add(s3, s3), origin.add(s3, s1))
        case Connection.TopRight => Line(color, origin.add(s1, s3), origin.add(s3, s3))
        case Connection.TopLeft => Line(color, origin.add(s3, s3), origin.add(s1, s3))
        case Connection.BottomRight => Line(color, origin.add(s1, s1), origin.add(s3, s1))
        case Connection.BottomLeft => Line(color, origin.add(s3, s1), origin.add(s1, s1))
      }
    }

    def connections(level: Int, origin: Point, side: Double, definition: HilbertDefinition): (Seq[Line], Seq[Line], Seq[Line]) = {
      println(s"connections $level $origin $side $definition")
      if level < 0 then (Seq.empty[Line], Seq.empty[Line], Seq.empty[Line])
      else {
        definition match {
          case HilbertDefinition(Orientation.Up, Direction.Clockwise) => 
            val a = Seq(line(col1, Connection.LeftUp, level, origin, side))
            val b = Seq(line(col2, Connection.TopRight, level, origin, side))
            val c = Seq(line(col3, Connection.RightDown, level, origin, side))
            (a, b, c)
          case HilbertDefinition(Orientation.Down, Direction.Clockwise) => 
            val a = Seq(line(col1, Connection.RightDown, level, origin, side))
            val b = Seq(line(col2, Connection.BottomLeft, level, origin, side))
            val c = Seq(line(col3, Connection.LeftUp, level, origin, side))
            (a, b, c)
          case HilbertDefinition(Orientation.Right, Direction.Clockwise) => 
            val a = Seq(line(col1, Connection.TopRight, level, origin, side))
            val b = Seq(line(col2, Connection.RightDown, level, origin, side))
            val c = Seq(line(col3, Connection.BottomLeft, level, origin, side))
            (a, b, c)
          case HilbertDefinition(Orientation.Left, Direction.Clockwise) => 
            val a = Seq(line(col1, Connection.TopLeft, level, origin, side))
            val b = Seq(line(col2, Connection.LeftDown, level, origin, side))
            val c = Seq(line(col3, Connection.BottomRight, level, origin, side))
            (a, b, c)
          case HilbertDefinition(Orientation.Up, Direction.CounterClockwise) => 
            val a = Seq(line(col1, Connection.RightUp, level, origin, side))
            val b = Seq(line(col2, Connection.TopLeft, level, origin, side))
            val c = Seq(line(col3, Connection.LeftDown, level, origin, side))
            (a, b, c)
          case HilbertDefinition(Orientation.Down, Direction.CounterClockwise) => 
            val a = Seq(line(col1, Connection.RightUp, level, origin, side))
            val b = Seq(line(col2, Connection.TopLeft, level, origin, side))
            val c = Seq(line(col3, Connection.LeftDown, level, origin, side))
            (a, b, c)
          case HilbertDefinition(Orientation.Right, Direction.CounterClockwise) => 
            val a = Seq(line(col1, Connection.BottomRight, level, origin, side))
            val b = Seq(line(col2, Connection.RightUp, level, origin, side))
            val c = Seq(line(col3, Connection.TopLeft, level, origin, side))
            (a, b, c)
          case HilbertDefinition(Orientation.Left, Direction.CounterClockwise) => 
            val a = Seq(line(col1, Connection.TopLeft, level, origin, side))
            val b = Seq(line(col2, Connection.LeftDown, level, origin, side))
            val c = Seq(line(col3, Connection.BottomRight, level, origin, side))
            (a, b, c)
        }
      }
    }

    def sides(level: Int, origin: Point, side: Double, definition: HilbertDefinition): (Seq[Line], Seq[Line], Seq[Line], Seq[Line]) = {
      println(s"sides $level")
      if level < 1 then (Seq.empty[Line], Seq.empty[Line], Seq.empty[Line], Seq.empty[Line])
      else {
        val s1 = side / 4.0
        val s3 = s1 * 2.0
        definition match {
          case HilbertDefinition(Orientation.Up, Direction.Clockwise) => 
            val a = drawRecursive(level, origin, side / 2.0, HilbertDefinition(Orientation.Right, Direction.Clockwise))
            val b = drawRecursive(level, origin.add(0, s3), side / 2.0, HilbertDefinition(Orientation.Up, Direction.Clockwise))
            val c = drawRecursive(level, origin.add(s3, s3), side / 2.0, HilbertDefinition(Orientation.Up, Direction.Clockwise))
            val d = drawRecursive(level, origin.add(s3, 0), side / 2.0, HilbertDefinition(Orientation.Left, Direction.CounterClockwise))
            (a, b, c, d)
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
    }

    def drawRecursive(level: Int, origin: Point, side: Double, definition: HilbertDefinition): Seq[Line] = {
      println(s"drawRecusive $level $side")
      val (c1, c2, c3) = connections(level - 1, origin, side, definition)
      val (s1, s2, s3, s4) = sides(level - 1, origin, side, definition)
      s1 ++ c1 ++ s2 ++ c2 ++ s3 ++ c3 ++ s4
    }

    val side = math.min(canvas.width, canvas.height).toDouble

    val origin = Point(0, 0)

    val startDefinition = HilbertDefinition(Orientation.Up, Direction.Clockwise)

    val re = drawRecursive(level, origin, side, startDefinition)
    println(startDefinition)
    println(origin)
    println(side)
    printPoli(re)
    re
  }

  def fmt(d: Double): String = f"${d}%.1f" 

  def fmt(p: Point): String = f"(${fmt(p.x)},${fmt(p.y)})"
  def fmt(l: Line): String = f"${fmt(l.start)}-${fmt(l.end)}"

  def printPoli(lines: Seq[Line]): Unit = {
    lines.foreach { l =>
      println(f"${fmt(l)}")
    }
  }

}
