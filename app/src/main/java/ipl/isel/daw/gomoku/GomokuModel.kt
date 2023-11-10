package ipl.isel.daw.gomoku

import androidx.compose.runtime.mutableIntStateOf

data class GomokuModel(val boardSize : Int) {

    val board = MutableList(boardSize) { MutableList(boardSize) { mutableIntStateOf(0) } }
    private var turn = 1
    private var winner = 0
    private var isFinished = false

    fun getTurn() : Int = turn
    fun getWinner() : Int = winner
    fun isFinished() : Boolean = isFinished

    fun play(x : Int, y : Int) : Boolean {
        if (board[x][y].intValue != 0 || isFinished) return false
        board[x][y].intValue = turn
        if (checkWin(x, y,getTurn())) {
            winner = turn
            isFinished = true
        }
        turn = if (turn == 1) 2 else 1
        return true
    }

    private fun checkWin(x: Int, y: Int, currentPlayer: Int): Boolean {
        return (checkDirection(x, y, currentPlayer, 1, 0) ||
                checkDirection(x, y, currentPlayer, 0, 1) ||
                checkDirection(x, y, currentPlayer, 1, 1) ||
                checkDirection(x, y, currentPlayer, 1, -1))
    }

    private fun checkDirection(x: Int, y: Int, currentPlayer: Int, dx: Int, dy: Int): Boolean {
        var count = 1
        var i = x + dx
        var j = y + dy
        while (i in 0 until boardSize && j >= 0 && j < boardSize && board[i][j].intValue == currentPlayer) {
            count++
            i += dx
            j += dy
        }
        return count >= 5
    }

    fun updateTurn(value: Int) { turn = value }

}
