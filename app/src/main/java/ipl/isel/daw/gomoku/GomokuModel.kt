package ipl.isel.daw.gomoku

data class GomokuModel(val boardSize : Int) {

    val board = MutableList(boardSize) { MutableList(boardSize) { 0 } }
    private var turn = 1
    private var winner = 0
    private var isFinished = false


    fun getTurn() : Int {
        return turn
    }

    fun getWinner() : Int {
        return winner
    }

    fun isFinished() : Boolean {
        return isFinished
    }

    fun play(x : Int, y : Int) : Boolean {
        if (board[x][y] != 0 || isFinished) return false
        board[x][y] = turn
        if (checkWin(x, y)) {
            winner = turn
            isFinished = true
        }
        turn = if (turn == 1) 2 else 1
        return true
    }

    private fun checkWin(x: Int, y: Int): Boolean {
        return checkHorizontal(x, y) || checkVertical(x, y) || checkDiagonal(x, y)
    }

    private fun checkDiagonal(x: Int, y: Int): Boolean {
        var count = 1
        var i = x - 1
        var j = y - 1
        while (i >= 0 && j >= 0 && board[i][j] == turn) {
            count++
            i--
            j--
        }
        i = x + 1
        j = y + 1
        while (i < boardSize && j < boardSize && board[i][j] == turn) {
            count++
            i++
            j++
        }
        if (count >= 5) return true
        count = 1
        i = x - 1
        j = y + 1
        while (i >= 0 && j < boardSize && board[i][j] == turn) {
            count++
            i--
            j++
        }
        i = x + 1
        j = y - 1
        while (i < boardSize && j >= 0 && board[i][j] == turn) {
            count++
            i++
            j--
        }
        return count >= 5
    }

    private fun checkVertical(x: Int, y: Int): Boolean {
        var count = 1
        var i = y - 1
        while (i >= 0 && board[x][i] == turn) {
            count++
            i--
        }
        i = y + 1
        while (i < boardSize && board[x][i] == turn) {
            count++
            i++
        }
        return count >= 5
    }

    private fun checkHorizontal(x: Int, y: Int): Boolean {
        var count = 1
        var i = x - 1
        while (i >= 0 && board[i][y] == turn) {
            count++
            i--
        }
        i = x + 1
        while (i < boardSize && board[i][y] == turn) {
            count++
            i++
        }
        return count >= 5
    }

}
