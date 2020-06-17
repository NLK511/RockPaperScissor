sealed trait Move {
  def toString: String
}

case object Rock extends Move {
  override def toString = "Rock"
}

case object Paper extends Move {
  override def toString = "Paper"
}

case object Scissor extends Move {
  override def toString = "Scissor"
}