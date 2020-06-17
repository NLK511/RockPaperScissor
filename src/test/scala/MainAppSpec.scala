import java.io.ByteArrayInputStream

import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.FlatSpec

class MainAppSpec extends FlatSpec {

  /**
   * We are fairly sure that each move will be chosen with the same probability,
   * so consider it as a test for demonstration purpose :) .
   * Since 1000 is a small number of trials to converge toward the expected value, I put an epsilon for tolerance in the
   * output of the test.*/
  "NextMove" should "return Rock, Paper or Scissor with approximately the same probability" in {
    implicit val doubleEquality: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(0.03)
    var counter: Seq[(Move, Double)] = Nil
    (1 to 1000).foreach { _ => counter ++= Seq((MainApp.nextMove, 1.0)) }

    val counterAgg: Map[Move, Double] = counter.groupBy(_._1).mapValues(_.map(_._2).sum / 1000)

    counterAgg.map { case (_, v) => assert(v === 0.33) }
  }

  /**
   * First all valid input are defined, then a stdin mock is created so that the test can ingest an input as if
   * someone is doing it from the keyboard.A bit verbose, but at least the coverage is almost total, invalid input branch
   * is not tested since it would require to feed console.withIn() with at least 2 lines, I don't know how to do that
   * and for the purpose of this demo I guess that the coverage is fine as it is.
   */
  "InsertMove" should "accept only valid moves" in {
    val inr = new ByteArrayInputStream("r".getBytes)
    val inrock = new ByteArrayInputStream("rock".getBytes)
    val inRock = new ByteArrayInputStream("Rock".getBytes)

    val inp = new ByteArrayInputStream("p".getBytes)
    val inpaper = new ByteArrayInputStream("paper".getBytes)
    val inPaper = new ByteArrayInputStream("Paper".getBytes)

    val ins = new ByteArrayInputStream("s".getBytes)
    val inscissor = new ByteArrayInputStream("scissor".getBytes)
    val inScissor = new ByteArrayInputStream("Scissor".getBytes)

    Console.withIn(inr) {
      assert(MainApp.insertMove == Rock)
    }
    Console.withIn(inrock) {
      assert(MainApp.insertMove == Rock)
    }
    Console.withIn(inRock) {
      assert(MainApp.insertMove == Rock)
    }
    Console.withIn(inp) {
      assert(MainApp.insertMove == Paper)
    }
    Console.withIn(inpaper) {
      assert(MainApp.insertMove == Paper)
    }
    Console.withIn(inPaper) {
      assert(MainApp.insertMove == Paper)
    }
    Console.withIn(ins) {
      assert(MainApp.insertMove == Scissor)
    }
    Console.withIn(inscissor) {
      assert(MainApp.insertMove == Scissor)
    }
    Console.withIn(inScissor) {
      assert(MainApp.insertMove == Scissor)
    }
  }

  /**
   * Here i simply exhaustively test if the method return the right outcome for each possibile match. Luckily there
   * are only 3^2 combinations :)
   **/
  "resolver" should "enforce RockPaperScissor game rules" in {
    assert(MainApp.resolver(Rock, Rock) == Draw)
    assert(MainApp.resolver(Rock, Paper) == Lose)
    assert(MainApp.resolver(Rock, Scissor) == Win)

    assert(MainApp.resolver(Paper, Rock) == Win)
    assert(MainApp.resolver(Paper, Paper) == Draw)
    assert(MainApp.resolver(Paper, Scissor) == Lose)

    assert(MainApp.resolver(Scissor, Rock) == Lose)
    assert(MainApp.resolver(Scissor, Paper) == Win)
    assert(MainApp.resolver(Scissor, Scissor) == Draw)
  }
}
