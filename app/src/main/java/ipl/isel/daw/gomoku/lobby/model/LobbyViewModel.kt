package ipl.isel.daw.gomoku.lobby.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipl.isel.daw.gomoku.TAG
import ipl.isel.daw.gomoku.game.model.GameOutputModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/*
 * View model for the Lobby Screen hosted by [LobbyActivity]
 */
class LobbyViewModel(
    private val lobbyService: RealLobbyService
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _type = MutableStateFlow(true)
    val type = _type.asStateFlow()

    private val _game = MutableStateFlow<GameOutputModel?>(null)
    val game = _game.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun chooseGameType() {
        _type.value = _type.value.not()
    }

    fun joinGame() =
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                _game.value = async { lobbyService.joinOrStartMatch(_type.value) }.await()
                if(_game.value == null)
                    _error.value = "Error joining game, API probably isn't Online"
                else
                    Log.v(TAG, "Game joined: ${_game.value}")
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
                val errorMessage = e.toString().split(": ").last()
                _error.value = errorMessage
            }
            _isLoading.value = false
        }


    fun resetError() {
        _error.value = null
    }

}
