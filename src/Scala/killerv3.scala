import java.io.File
import scala.io.Source

case class Position(solved: Boolean, board: Array[Int])

@main def killer =
  val solution = recursiveCheck(Position(false, Array.fill(81)(0)))
  println(solution.board.mkString(" "))

val rules: Array[Array[Int]] = Source.fromFile("puzzle.csv").getLines
  .map(_.split(",").map(_.toInt))
  .flatMap(el => el.tail.map( (_, el) )).toList.sortBy(_._1).map(_._2).toArray

val relatedCells: Array[Array[Int]] =
  val sameRow = (i: Int, j: Int) => i / 9 == j / 9
  val sameCol = (i: Int, j: Int) => (i - j) % 9 == 0
  val sameBlock = (i: Int, j: Int) => (i / 27 == j / 27) && (i % 9 / 3 == j % 9 / 3)
  val isRelated = (cell: Int) => (el: Int) => (sameRow(cell, el) || sameCol(cell, el) || sameBlock(cell, el))
  val getRelatedCells = (cell: Int) => (0 to 80).filter(isRelated(cell)).toArray

  (0 to 80).map(getRelatedCells).toArray

val recursiveCheck: Position => Position = pos =>
  val i = pos.board.indexOf(0)
  if i == -1 then Position(true, pos.board)
  else
    val validAttempt = (m: Int) => !(relatedCells(i).map(pos.board) contains m)
      && (rules(i).last != i
      || rules(i).tail.map(pos.board).sum + m == rules(i).head)

    val step = (pos: Position, m: Int) =>
      if !pos.solved && validAttempt(m) then recursiveCheck(Position(false, pos.board.updated(i, m))) match
        case Position(false, _) => pos 
        case solved => solved
      else pos

    (1 to 9).foldLeft(Position(false, pos.board))(step)
