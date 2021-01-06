import scala.io.Source
import scala.collection.mutable.Set
import java.io.File

object Sudoku extends App {
  
  //Convert a CSV file into a list of integer arrays
  def getParms(): List[Array[Int]] = Source.fromFile("resources" + File.separator + "puzzle.csv")
    .getLines
    .map(line => line.split(",").map(_.toInt))
    .toList
    .sortBy(_.last)
 
  //Check the rules for the cell against the given board. A rule is an array where the first item
  //is the required total and the remaining items are the cell positions that must be summed"
  def checkAllRules(board: Array[Int], filteredParms:List[Array[Int]], m:Int): Boolean = {
    for (p <- filteredParms) {
      var cells = p.drop(1).map(i => board.apply(i))
      cells(cells.length - 1) = m
      if (!cells.contains(0))
        if (p(0) != cells.sum)
          return false
    }
    return true
  }

  //Check the board is valid - the value m at position i is allowed
  def checkBoardValid(board: Array[Int], i:Int, m:Int): Boolean = {
    def sameRow(i: Int, j: Int): Boolean = i / 9 == j / 9
    def sameCol(i: Int, j:Int): Boolean = (i - j) % 9 == 0 
    def sameBlock(i: Int, j:Int):Boolean = ((i / 27 == j / 27) && (i%9/3 == j%9/3))

    for (j <- 0 to 80 if i != j && board(j) != 0)
      if (sameRow(i, j) || sameCol(i, j) || sameBlock(i, j))
        if (board(j) == m)
          return false

    return true
  }

  //Finds the first non-zero cell then try the values 1, 2, 3...
  //If the attempt satisfies the rules the recurse to solve this new position"
  def recursiveCheck(board: Array[Int]): Unit = {
    val i = board.indexOf(0)
    if (i == -1) {
      println(s"${board.mkString(" ")}")
      val t2 = (System.currentTimeMillis() - t1) / 1000.0
      println("finished. elapsed time = " + t2 + " seconds")
      System.exit(0)
    }
    
    for (m <- 1 to 9) {
      if (checkBoardValid(board, i, m)) 
        if (checkAllRules(board, parms.filter(_.last == i), m)) {
          var newBoard = board.clone()
          newBoard(i) = m
          recursiveCheck(newBoard)
        }
    }
  }

  val parms: List[Array[Int]] = getParms()
  val board = Array.fill(81)(0)
  println("started...")
  val t1 = System.currentTimeMillis()
  recursiveCheck(board)
}
