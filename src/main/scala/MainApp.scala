import com.typesafe.config.ConfigFactory

import scala.annotation.tailrec
import scala.util.Random

object MainApp {

  def main(args: Array[String]) {
    val applicationConfig = ConfigFactory.parseFile(new java.io.File("src/main/conf/app.conf"))

    /** I like to package all configuration values of an app inside a context object. In this way I have a unique access
     * point for all params and also I hide the logic to extract and process the config files.  */
    val ctx = new AppContext(applicationConfig)

    println("### Welcome to RockPaperScissor Scala edition! ###")
    println("Conf params:")
    println(s"Running in ${ctx.mode} mode")
    println("----------------------")

    /** Using case objects as enums I improve greatly the readability of the code at the price of having some files more
     * where I define them. In my experience it's always worth it, especially with a good folder organization. Sometimes
     * the trait or the object can also package some special function that further simplify the main code */
    ctx.mode match {
      case Cpu => println("Player1 " + resolver(nextMove, nextMove) + " the game")
      case Single => println("Player1 " + resolver(insertMove(), nextMove) + " the game")
      case Multi => println("Player1 " + resolver(insertMove(), insertMove()) + " the game")
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
  def insertMove(): Move = {
    println("Valid moves are r, p, s, rock, paper, scissor, Rock, Paper, Scissor")
    println("Insert your move: ")
    val a = scala.io.StdIn.readLine()
    a match {
      case "R" | "r" | "Rock" | "rock" => Rock
      case "P" | "p" | "Paper" | "paper" => Paper
      case "S" | "s" | "Scissor" | "scissor" => Scissor
      case _ =>
        println("Invalid move, please insert a valid one.")
        insertMove()
    }
  }

  /** Resolver function establish who's gonna win the match. To make it simpler it always give the outcome from the point
   * of view of the first player(Hero)
   * */
  def resolver(heroMove: Move, opponentMove: Move): Outcome = {
    println(s"Player1 throw a $heroMove... ")
    println(s"Player2 throw a $opponentMove... ")
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
