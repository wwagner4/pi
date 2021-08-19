import org.scalatest.funsuite.AnyFunSuite
import pi.ColorIterator

import scala.io.Source

class PiTests extends AnyFunSuite {

  test("read pi") {
    for ((c, i) <- ColorIterator.pi(ColorIterator.seqZero).zipWithIndex) {
      println(s"${i} - $c")
    }
  }

}
