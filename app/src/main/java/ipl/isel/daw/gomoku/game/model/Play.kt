package ipl.isel.daw.gomoku.game.model

import java.util.UUID

data class Game(
    val id: UUID,
    val board: Board,
    val userId1: UUID,
    val userId2: UUID,
    val isFinished: Boolean,
    val winner: UUID?
)

data class GameOpenDTO(val uuid: String, val player1: String, val spr: Int)
data class GameEndedDTO(
    val uuid: String,
    val player1: String,
    val player2: String,
    val result: Int,
)

data class GameDTO(
    val uuid: UUID,
    val state: String,
    val nextPlay: String,
    val player1: UUID,
    val player2: UUID,
    val board: String,
    val moves1: String,
    val moves2: String,
    val result: Int,
)

data class State(val status: String) // { WAITING, BUILDING, STARTED, ENDED }

/**
 * Class que permite ser alterada após uma jogada, para permitir que o jogador saiba
 * quando é que pode jogar
 */


data class Room(val uuid: UUID, val player1: String)

data class GameIdModel(val gameId: UUID)


data class GameBoard(val board: String)

data class GameLastState(
    val id: UUID,
    val board: Board,
    val userId1: UUID,
    val userId2: UUID,
    val isFinished: Boolean,
    val winner: UUID?
)

enum class Turn { PLAYER1, PLAYER2 }
enum class Loader { WAITING, STARTED, ENDED }

data class StateGame(
    val gameState: Loader?,
    val turn: String,
    var board: String,
    val result: Int?
)

data class HitOrMiss(val hitOrMiss: List<Boolean>)


data class GamePlayInputModel(
    val userId: UUID,
    val l: Int,
    val c: Int
)

data class GameStartInputModel(
    val userId1: UUID,
    val userId2: UUID? = null,
    val traditional: Boolean = false
)

data class GameOutputModel(
    val id: UUID,
    val board: BoardAPI,
    val userId1: UUID,
    val userId2: UUID,
    val isFinished: Boolean,
    val winner: UUID?
)

data class GameStringModel(
    val id: UUID,
    val board: String,
    val userId1: UUID,
    val userId2: UUID,
    val isFinished: Boolean,
    val winner: UUID?
)

/*fun String.toBoard(traditional: Boolean): Board {
    val ratio = if (traditional) 15 else 19
    val cells = Array(ratio) { Array(ratio) { CellState.EMPTY } }
    this.forEachIndexed { index, c ->
        val cell = CellState.fromChar(c)
        if (cell == CellState.PLAYER_ONE || cell == CellState.PLAYER_TWO) {
            cells[index / ratio][index % ratio] = CellState.fromChar(c)
        }
    }
    return Board(traditional).updateBoard(cells)
}*/

