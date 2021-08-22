package pi

import pi.Main.{Canvas, Line, Point}
import java.awt.Color

object HilbertTurtle {

  enum Direction:
    case Up, Down, Right, Left

  class Turtle(canvas: Canvas, colors: Iterator[Color], startDirection: Direction, startPosition: Point) {
    var _direction: Direction = startDirection
    var _position: Point = startPosition

    def forward(len: Double): Turtle = {
      val a = this._position
      val b = (this._direction match {
        case Direction.Up => this._position.add(0, len)
        case Direction.Down => this._position.add(0, -len)
        case Direction.Right => this._position.add(len, 0)
        case Direction.Left => this._position.add(-len, 0)
      })
      val a1 = a.add(0, 1)
      val b1 = b.add(0, 1)
      canvas.line(colors.next(), a1, b1)
      //println(f"draw ${a1.x}%.2f ${a1.y}%.2f - ${b1.x}%.2f ${b1.y}%.2f")
      this._position = b
      this
    }

    def turnRight: Turtle = {
      this._direction match {
        case Direction.Up => this._direction = Direction.Right
        case Direction.Down => this._direction = Direction.Left
        case Direction.Right => this._direction = Direction.Down
        case Direction.Left => this._direction = Direction.Up
      }
      this
    }

    def turnLeft: Turtle = {
      this._direction match {
        case Direction.Up => this._direction = Direction.Left
        case Direction.Down => this._direction = Direction.Right
        case Direction.Right => this._direction = Direction.Up
        case Direction.Left => this._direction = Direction.Down
      }
      this
    }
  }


  def draw(level: Int, canvas: Canvas, colorIterator: Iterator[Color]): Unit = {

    val l = math.pow(2, level)
    val len = math.ceil(canvas.size.toDouble / (l + 2))
    // println(s"l/width/l/len: $level/$w/$l/$len")
    val turtle = Turtle(canvas, colorIterator, Direction.Up, Point(len - 1, len - 1))

    def drawClockwise(level: Int, turtle: Turtle): Unit = {
      if level >= 0 then {
        turtle.turnRight
        drawCounterClockwise(level - 1, turtle)
        turtle.turnRight
        if level > 0 then turtle.forward(len)
        drawClockwise(level - 1, turtle)
        turtle.turnLeft
        if level > 0 then turtle.forward(len)
        turtle.turnLeft
        drawClockwise(level - 1, turtle)
        if level > 0 then turtle.forward(len)
        turtle.turnRight
        drawCounterClockwise(level - 1, turtle)
        turtle.turnRight
      }
    }

    def drawCounterClockwise(level: Int, turtle: Turtle): Unit = {
      if level >= 0 then {
        turtle.turnLeft
        drawClockwise(level - 1, turtle)
        turtle.turnLeft
        if level > 0 then turtle.forward(len)
        drawCounterClockwise(level - 1, turtle)
        turtle.turnRight
        if level > 0 then turtle.forward(len)
        turtle.turnRight
        drawCounterClockwise(level - 1, turtle)
        if level > 0 then turtle.forward(len)
        turtle.turnLeft
        drawClockwise(level - 1, turtle)
        turtle.turnLeft
      }
    }


    drawClockwise(level, turtle)
  }

}

