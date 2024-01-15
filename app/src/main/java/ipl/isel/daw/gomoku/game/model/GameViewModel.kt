package ipl.isel.daw.gomoku.game.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

private const val emptyBoard = "________________________________________________________________"

class GameViewModel (
    private val gameService: PlayService,
    private val info: MatchInfo
) : ViewModel() {

    private var _isReady by mutableStateOf(false)
    val isReady: Boolean
        get() = _isReady

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading

    private val _currentGame = MutableStateFlow(StateGame( Loader.WAITING, "", "", null ))
    val currentGame = _currentGame.asStateFlow()

    private val _myTurn = MutableStateFlow(false)
    val myTurn = _myTurn.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _hit = MutableStateFlow(false)
    val hit = _hit.asStateFlow()

    private val _currentlyPlacing = MutableStateFlow<Piece?>(null)
    val currentlyPlacing = _currentlyPlacing.asStateFlow()

    private val _placedGoPieces = MutableStateFlow<ArrayList<Piece>>(arrayListOf())
    val placedGoPieces = _placedGoPieces.asStateFlow()

    private val _playerBoard = MutableStateFlow(Board())
    val playerBoard = _playerBoard.asStateFlow()

    private val _processedInfo = MutableStateFlow(info)
    val processedInfo = _processedInfo.asStateFlow()

    fun isMyTurn() = _myTurn.value

    fun resetBoard(){
        _placedGoPieces.value = arrayListOf()
    }

    private fun buildingPlacements(): Array<Piece> {
        val newBoard = Board()
        placedGoPieces.value.forEachIndexed { index, go  ->
            newBoard.cells[index] = go //TODO check
        }
        return newBoard.cells
    }

    fun placePiece() {
        _placedGoPieces.value.add(currentlyPlacing.value!!)
        _playerBoard.value.cells = buildingPlacements()
        _currentlyPlacing.value = null
    }

/*    fun gameStart() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                gameService.buildBoard(info.uuid)
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
                val errorMessage = e.toString().split(": ").last()
                _error.value = errorMessage
            }
        }
    }*/


    @OptIn(ExperimentalCoroutinesApi::class)
    fun refreshGame() {
        val result = viewModelScope.async(Dispatchers.IO) {
            try {
                gameService.refreshedGameState(info.uuid)
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
                if(game != null) {
                    val newBoard = game.board.toPiecedBoard()
                    _placedGoPieces.value = newBoard.cells.toCollection(arrayListOf())
                    _myTurn.value = checkIfMyTurn(game.board, info)
                    _currentGame.value =
                        StateGame(
                            Loader.STARTED,
                            if (_myTurn.value) R.string.game_myturn.toString()
                            else  R.string.game_enemyturn.toString(),
                            newBoard.toString(),
                            null
                        )
                    _isReady = true
                }
                //_currentGame.value = StateGame(Loader.WAITING, "", "", null)

                if (currentGame.value.gameState == Loader.WAITING) {
                    currentGame.value.board = ""
                }

                _playerBoard.value = Board(currentGame.value.board)
            }
        }
    }

    private fun checkIfMyTurn(board: BoardAPI, info: MatchInfo): Boolean {
        // count the number of cells for each player in info
        val player1 = board.cells.flatten().count { it == ApiCells.PLAYER_ONE.toString() }
        val player2 = board.cells.flatten().count { it == ApiCells.PLAYER_TWO.toString() }

        // if player1 has more cells than player2, and you are player2 it's player2's turn
        info.whoAmI.let {
            return if (player1 > player2) it == "PLAYER2"
            else it == "PLAYER1"
        }

    }

    fun makeMove(piece: Pair<Int,Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            _hit.value =
                try {
                    gameService.makeMove(info.uuid, piece)
                } catch (e: Exception) {
                    Log.v(TAG, e.toString())
                    val errorMessage = e.toString().split(": ").last()
                    _error.value = errorMessage
                    false
                }
            if(_hit.value) {
                _myTurn.value = false
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


    fun canPlaceCurrentPiece(piece: Piece, l: Int, c: Int): Boolean {
        if(_currentGame.value.board[ l * BOARD_ROWS + c] == Type.SPACE.get()) {
            _currentlyPlacing.value!!.type = piece.type
            return true
        }
        return false
    }

    fun resetError() {
        _error.value = null
    }

}