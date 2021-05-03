object Sudoku extends App {

  val killerRules: Map[Int, List[Int]] =
    scala.io.Source
      .fromFile("C:\\Users\\simon\\scala\\scala-advanced\\src\\exercises\\puzzle.csv").getLines.toList
      .map(line => line.split(",").toList.map(_.toInt))
      .flatMap(el => el.drop(1).map(v => Map(v -> el)))
      .reduce(_ ++ _)

  val relatedCell: List[List[Int]] = {
    def getRelated(cell: Int): List[Int] = {
      def sameRow(i: Int, j: Int): Boolean = i / 9 == j / 9
      def sameCol(i: Int, j: Int): Boolean = (i - j) % 9 == 0
      def sameBlock(i: Int, j: Int): Boolean = (i / 27 == j / 27) && (i % 9 / 3 == j % 9 / 3)

      (0 to 80).flatMap({ el =>
          if (cell != el && (sameRow(cell, el) || sameCol(cell, el) || sameBlock(cell, el)))
            List(el)
          else List()
        }).toList
    }
    (0 to 80).map(getRelated).toList
  }

  def recursiveCheck(board: Array[Int]): Unit = {
    val i = board.indexOf(0)
    if (i == -1) {
      println(s"${board.mkString(" ")}")
      System.exit(0)
    }

    for (m <- 1 to 9) {
      if (!(relatedCell(i).map(board) contains m))
        if (killerRules(i).last != i
        || killerRules(i).drop(1).map(board).sum + m == killerRules(i).head)
          recursiveCheck(board.updated(i, m))
    }
  }

  val board = Array.fill(81)(0)
  println("started...")
  recursiveCheck(board)
