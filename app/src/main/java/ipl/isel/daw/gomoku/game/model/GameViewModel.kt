package ipl.isel.daw.gomoku.game.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipl.isel.daw.gomoku.R
import ipl.isel.daw.gomoku.TAG
import ipl.isel.daw.gomoku.lobby.MatchInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameService: PlayService,
    private val info: MatchInfo
) : ViewModel() {

    private val _currentGame = MutableStateFlow(StateGame(Loader.WAITING, "", "", null))
    val currentGame = _currentGame.asStateFlow()

    private val _myTurn = MutableStateFlow(false)
    val myTurn = _myTurn.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _playerBoard = MutableStateFlow<Board?>(null)
    val playerBoard = _playerBoard.asStateFlow()

    private val _processedInfo = MutableStateFlow(info)
    val processedInfo = _processedInfo.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun refreshGame() {
        val result = viewModelScope.async(Dispatchers.IO) {
            try {
                gameService.refreshedGameState(_processedInfo.value.uuid)
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
                val errorMessage = e.toString().split(": ").last()
                _error.value = errorMessage
                null
            }
        }
        result.invokeOnCompletion {
            if (it == null) { // if no exception
                val game = result.getCompleted()
                if (game != null) {
                    val newBoard = game.board.toPiecedBoard()
                    _myTurn.value = checkIfMyTurn(game.board, info)
                    _currentGame.value =
                        StateGame(
                            if(game.isFinished) Loader.ENDED else Loader.STARTED,
                            if (_myTurn.value) R.string.game_myturn.toString()
                            else R.string.game_enemyturn.toString(),
                            newBoard.toString(),
                            null
                        )
                }

                if (currentGame.value.gameState == Loader.WAITING)
                    currentGame.value.board = ""

                _playerBoard.value = Board(_currentGame.value.board)
            }
        }
    }

    private fun checkIfMyTurn(board: BoardAPI, info: MatchInfo): Boolean {
        // count the number of cells for each player in info
        val flat = board.cells.flatten()
        val player1 = flat.count { it == ApiCells.PLAYER_ONE.name }
        val player2 = flat.count { it == ApiCells.PLAYER_TWO.name }

        // if player1 has more cells than player2, and you are player2 it's player2's turn
        info.whoAmI.let {
            return if (player1 > player2) it == Turn.PLAYER2.name
            else it == Turn.PLAYER1.name
        }
    }

    fun makeMove(piece: Pair<Int, Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                gameService.makeMove(info.uuid, piece)
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
                val errorMessage = e.toString().split(": ").last()
                _error.value = errorMessage
            }
        }
    }

    fun handleForfeit() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                gameService.forfeitMatch(info.uuid)
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
            }
        }
    }

    fun resetError() {
        _error.value = null
    }

}