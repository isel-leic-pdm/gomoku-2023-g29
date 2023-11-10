package ipl.isel.daw.gomoku

import androidx.compose.runtime.MutableState

data class GameConditions(
    val model: GomokuModel,
    val gameState: MutableState<Boolean>,
    val turn: MutableState<Int>
)
