import org.scalatest.funsuite.AnyFunSuite
import pi.ColorIterator

import java.awt.Color
import scala.io.Source

class PiTests extends AnyFunSuite {

  test("tryout") {
    val seq = 0 to 9
    LazyList.continually(seq).flatten.take(20).foreach(println(_))

  }


}
