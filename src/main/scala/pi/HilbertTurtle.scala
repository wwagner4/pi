package pi

import pi.Main.{Canvas, Line, Point}
import java.awt.Color

object HilbertTurtle {

    enum Direction :
        case Up, Down, Right, Left

    class Turtle(direction: Direction, position: Point) {
        var _direction: Direction = direction 
        var _position: Point = position 
        var _path: List[Line] = List() 

        def forward(len: Double, level: Int): Turtle = {
            val a = this._position
            val b = this._direction match {
                case Direction.Up => this._position.add(0, len)
                case Direction.Down => this._position.add(0, -len)
                case Direction.Right => this._position.add(len, 0)
                case Direction.Left => this._position.add(-len, 0)
            }
            this._path = Line(Color.BLACK, a, b) :: this._path
            this._position = b
            println(s"$level fw ${this._position}")
            this
        }

        def turnRight(level: Int): Turtle = {
            this._direction match {
                case Direction.Up => this._direction = Direction.Right
                case Direction.Down => this._direction = Direction.Left
                case Direction.Right => this._direction = Direction.Down
                case Direction.Left => this._direction = Direction.Up
            }
            println(s"$level tr ${this._direction}")
            this
        }

        def turnLeft(level: Int): Turtle = {
            this._direction match {
                case Direction.Up => this._direction = Direction.Left
                case Direction.Down => this._direction = Direction.Right
                case Direction.Right => this._direction = Direction.Up
                case Direction.Left => this._direction = Direction.Down
            }
            println(s"$level tl ${this._direction}")
            this
        }
    }


    def draw(level: Int, canvas: Canvas): Seq[Line] = {

        val w = math.min(canvas.width, canvas.height)
        val l = math.pow(2, level)                    
        val len = w.toDouble / l
        val turtle = Turtle(Direction.Up, Point(len / 2, len / 2))

        def drawClockwise(level: Int, turtle: Turtle): Unit = {
            if level >= 0 then {
                turtle.turnRight(level)
                drawCounterClockwise(level - 1, turtle)
                turtle.turnRight(level)
                if level > 0 then turtle.forward(len, level)
                drawClockwise(level - 1, turtle)
                turtle.turnLeft(level)
                if level > 0 then turtle.forward(len, level)
                turtle.turnLeft(level)   
                drawClockwise(level - 1, turtle)
                if level > 0 then turtle.forward(len, level)
                turtle.turnRight(level)
                drawCounterClockwise(level - 1, turtle)
                turtle.turnRight(level)
            }
        } 

        def drawCounterClockwise(level: Int, turtle: Turtle): Unit = {
            if level >= 0 then {
                turtle.turnLeft(level)
                drawClockwise(level - 1, turtle)
                turtle.turnLeft(level)
                if level > 0 then turtle.forward(len, level)
                drawCounterClockwise(level - 1, turtle)
                turtle.turnRight(level)
                if level > 0 then turtle.forward(len, level)
                turtle.turnRight(level)   
                drawCounterClockwise(level - 1, turtle)
                if level > 0 then turtle.forward(len, level)
                turtle.turnLeft(level)
                drawClockwise(level - 1, turtle)
                turtle.turnLeft(level)
            }
        } 


        drawClockwise(level, turtle)

        turtle._path.reverse
    }

}

