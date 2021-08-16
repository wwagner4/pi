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

        val len = 10
        val turtle = 
            Turtle(Direction.Up, Point(canvas.width / 2.0, canvas.height / 2.0))
                .forward(len)
                .turnRight   
                .forward(len)
                .turnRight   
                .forward(len)

        val re = turtle._path.reverse
        println(re)
        re
    }

}

