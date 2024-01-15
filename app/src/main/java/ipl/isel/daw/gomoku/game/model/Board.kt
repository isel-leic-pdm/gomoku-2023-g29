package ipl.isel.daw.gomoku.game.model

import kotlin.math.sqrt

const val BOARD_ROWS = 15

private const val BOARD_SIZE = BOARD_ROWS * BOARD_ROWS


enum class ApiCells(val char: Char) {
    EMPTY('+'),
    PLAYER_ONE('1'),
    PLAYER_TWO('2');

    companion object {
        fun fromChar(char: Char): ApiCells {
            return when (char) {
                '+' -> EMPTY
                '1' -> PLAYER_ONE
                '2' -> PLAYER_TWO
                else -> throw IllegalArgumentException("Invalid value for CellState")
            }
        }
    }
}


data class BoardAPI(val gamesize: Boolean, val cells: Array<Array<String>>, val size: Int) {

    fun toPiecedBoard(): Board {
        val board = Board(size*size)
        cells.forEachIndexed { i, column ->
            column.forEachIndexed { j, row ->
                board.cells[i * size + j] =
                    when(row){
                        ApiCells.PLAYER_ONE.toString() -> Piece(Type.PLAYER_1)
                        ApiCells.PLAYER_TWO.toString() -> Piece(Type.PLAYER_2)
                        else -> Piece(Type.SPACE)
                    }
            }
        }
        return board
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BoardAPI

        if (gamesize != other.gamesize) return false
        return cells.contentDeepEquals(other.cells)
    }

    override fun hashCode(): Int {
        var result = gamesize.hashCode()
        result = 31 * result + cells.contentDeepHashCode()
        return result
    }
}


class Board{
    private var size: Int? = null
    var cells: Array<Piece>

    constructor() {
        this.cells = Array(BOARD_SIZE) { Piece(Type.SPACE) }
    }

    constructor(size: Int) {
        this.size = size
        this.cells = Array(size) { Piece(Type.SPACE) }
    }

    constructor(boardString: String) {
        val board = Board().cells
        val size = size ?: BOARD_SIZE
        val sqr = sqrt(size.toDouble()).toInt() // 15 * 15 = 225 -> 15 | 19 * 19 = 361 -> 19
        val chunkedBoard = boardString.chunked(sqr).map{ it.reversed() }

        for (i in 0 until sqr){
            for (j in 0 until sqr) {
                board[i * sqr + j] = chunkedBoard[i][j].toPiece()
            }
        }
        this.cells = board
    }

    override fun toString() : String {
        val microsoftWord : StringBuilder = StringBuilder()
        cells.forEach { microsoftWord.append(it.type.get()) }
        return microsoftWord.toString()
    }

}

class GoPiece (var l :Int=0, var c :Int=0)

fun indexToCoordinates (index: Int): Pair<Int,Int> {
    val row = index / BOARD_ROWS + 1
    val col = index % BOARD_ROWS + 1
    return Pair(row, col)
}

fun getIndexes(goPiece: GoPiece): ArrayList<Int> {
    val indices: ArrayList<Int> = ArrayList()
    indices.add(BOARD_ROWS * goPiece.l + goPiece.c)
    return indices
}
