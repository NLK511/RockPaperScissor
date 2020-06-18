//import com.typesafe.config.ConfigFactory

import scala.annotation.tailrec
import scala.util.Random

object MainApp {

  def main(args: Array[String]) {

    /**
     * In a first version of the software I didn't realized that it was not allowed to use external libraries, so I used
     * typesafe config to make the app configurable from a file. Whenever you cross some commented line it is probably
     * code about this, I have decided to leave it here in case you might find it interesting.
     */
    //    val applicationConfig = ConfigFactory.parseFile(new java.io.File("src/main/conf/app.conf"))
    //    val ctx = new AppContext(applicationConfig)

    /** I like to package all configuration values of an app inside a context object. In this way I have a unique access
     * point for all params and also I hide the logic to extract and process the config files.  In this case there is only
     * one param(mode) and enum definition so it's not really necessary, but a real application normally has an ever
     * increasing amount of config params. If the project contains multiple apps, and multiple contexts are defined, I like
     * to define a context trait that contains all common helper functions, and then extends(inherit) my app specific
     * context from it.
     * */
    implicit val ctx: AppContext = new AppContext()

    println("### Welcome to RockPaperScissor Scala edition! ###")
    println("Conf params:")
    println(s"Running in ${ctx.mode} mode")
    println("----------------------")

    /** Using case objects as enums I improve greatly the readability of the code at the price of having some files more
     * where I define them. In my experience it's always worth it, especially with a good folder organization. Sometimes
     * the trait or the object can also package some special function that further simplify the main code */
    ctx.mode match {
      case Cpu => (1 to ctx.nMatch).foreach{_ => println(s"${ctx.player1Name} " + resolver(nextMove, nextMove) + " the match")}
      case Single => (1 to ctx.nMatch).foreach{_ => println(s"${ctx.player1Name} " + resolver(insertMove(ctx.player1Name), nextMove) + " the match")}
      case Multi => (1 to ctx.nMatch).foreach{_ => println(s"${ctx.player1Name} " + resolver(insertMove(ctx.player1Name), insertMove(ctx.player2Name)) + " the match")}
    }
  }

  /** A function is defined within this function, the use of private function helps to make clear what the domain of application,
   * of a function should be, improving code readibility and manteinance.
   */
  def nextMove: Move = {
    def intToMove(n: Int): Move = {
      n match {
        case 0 => Rock
        case 1 => Paper
        case 2 => Scissor
      }
    }

    intToMove(Random.nextInt(3))
  }

  @tailrec
  def insertMove(playerName: String): Move = {
    println("Valid moves are r, p, s, rock, paper, scissor, Rock, Paper, Scissor")
    println(s"$playerName Insert your move: ")
    val a = scala.io.StdIn.readLine()
    a match {
      case "R" | "r" | "Rock" | "rock" => Rock
      case "P" | "p" | "Paper" | "paper" => Paper
      case "S" | "s" | "Scissor" | "scissor" => Scissor
      case _ =>
        println("Invalid move, please insert a valid one.")
        insertMove(playerName)
    }
  }

  /** Resolver function establish who's gonna win the match. To make it simpler it always give the outcome from the point
   * of view of the first player(Hero)
   * */
  def resolver(heroMove: Move, opponentMove: Move)(implicit ctx: AppContext): Outcome = {
    println(s"${ctx.player1Name} throw a $heroMove... ")
    println(s"${ctx.player2Name} throw a $opponentMove... ")
    if (heroMove.equals(opponentMove)) Draw
    else {
      heroMove match {
        case Rock if opponentMove == Paper => Lose
        case Paper if opponentMove == Scissor => Lose
        case Scissor if opponentMove == Rock => Lose
        case _ => Win
      }
    }
  }
}
