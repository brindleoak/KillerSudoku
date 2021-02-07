import java.io.File
import java.sql.Timestamp
import java.util.*
import java.util.stream.IntStream
import kotlin.system.exitProcess

object Killer {

    private var params = HashMap<Int, List<Int>>()
    private var combos = Array(81) {Array(81) {false} }

    @JvmStatic
    fun main(args: Array<String>) {
        println(Timestamp(System.currentTimeMillis()))
        val intVals: List<List<Int>> = File("/home/sandy/puzzle.csv").readLines().map { it -> it.split(",").map { it.toInt() } }
        intVals.forEach { line -> params[line[line.size - 1]] = line }
        println("params: $params")
        for(i:Int in 0 until 81) {
            for (j: Int in 0 until i) {
                combos[i][j] = (sameRow(i, j) || sameCol(i, j) || sameBlock(i, j))
            }
        }
        val newBoard = IntArray(81)
        recursiveCheck(newBoard)
    }

    private fun getSelectedParam(position: Int): List<Int>? {
        return if (params[position] != null)
            params[position]
        else
        //return arrayListOf<Int>()
            emptyList()
    }

    private fun sameRow(i: Int, j: Int): Boolean {
        //return i / 9 shr 0 == j / 9 shr 0
        return (i/9) == (j/9)
    }

    private fun sameCol(k: Int, l: Int): Boolean {
        return (k - l) % 9 == 0
    }

    private fun sameBlock(m: Int, n: Int): Boolean {
        return m / 27 == n / 27 && m % 9 / 3 == n % 9 / 3
    }

    private fun paramTotal(param: List<Int>?, aRow: IntArray, value: Int): Int? {
        val subParam = param?.subList(1, param.size - 1)
        return subParam?.stream()?.reduce(value) { subtotal: Int, element: Int? ->
            subtotal + aRow[element!!]
        }
    }

    private fun checkRow(aRow: IntArray, position: Int, value: Int): Boolean {
        val selectedParam = getSelectedParam(position)
        return if (selectedParam.isNullOrEmpty()) {
            true
        } else {
            val total = paramTotal(selectedParam, aRow, value)
            total == selectedParam[0]
        }
    }

    private fun recursiveCheck(inBoard: IntArray): Boolean {
        val pos = IntStream.range(0, inBoard.size).filter { i: Int -> inBoard[i] == 0 }.findFirst().orElse(-1)
        if (pos == -1) {
            println("results:" + inBoard.contentToString())
            println(Timestamp(System.currentTimeMillis()))
            // return true;
            exitProcess(1)
        }
        val excludedNumbers: MutableSet<Int> = HashSet()
        for (x in 0 until pos) {
            if (combos[pos][x]) {
                excludedNumbers.add(inBoard[x])
            }
        }
        for (y in 1..9) {
            if (!excludedNumbers.contains(y)) {
                if (checkRow(inBoard, pos, y)) {
                    val newBoard = inBoard.clone()
                    newBoard[pos] = y
                    recursiveCheck(newBoard)
                }
            }
        }
        return true
    }
}
