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

        def forward(len: Double): Turtle = {
            val a = this._position
            val b = this._direction match {
                case Direction.Up => this._position.add(0, len)
                case Direction.Down => this._position.add(0, -len)
                case Direction.Right => this._position.add(len, 0)
                case Direction.Left => this._position.add(-len, 0)
            }
            this._path = Line(Color.BLACK, a, b) :: this._path
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


    def draw(level: Int, canvas: Canvas): Seq[Line] = {

        val w = math.min(canvas.width, canvas.height)
        val l = math.pow(2, level)                    
        val len = w.toDouble / l
        val turtle = Turtle(Direction.Up, Point(len / 2, len / 2))

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

        turtle._path.reverse
    }

}

