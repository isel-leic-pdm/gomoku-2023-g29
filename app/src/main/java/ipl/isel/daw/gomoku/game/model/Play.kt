package ipl.isel.daw.gomoku.game.model

import java.util.UUID

data class HitOrMiss(val hitOrMiss: List<Boolean>)

data class GameBoard(val board: String)

enum class Turn { PLAYER1, PLAYER2 }

enum class Loader { WAITING, STARTED, ENDED }

data class StateGame(
    val gameState: Loader?,
    val turn: String,
    var board: String,
    val result: Int?
)

data class GameOutputModel(
    val id: UUID,
    val board: BoardAPI,
    val userId1: UUID,
    val userId2: UUID,
    val isFinished: Boolean,
    val winner: UUID?
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