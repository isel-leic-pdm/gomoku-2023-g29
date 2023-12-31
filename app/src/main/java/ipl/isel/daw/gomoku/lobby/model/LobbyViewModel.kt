package ipl.isel.daw.gomoku.lobby.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipl.isel.daw.gomoku.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * View model for the Lobby Screen hosted by [LobbyActivity]
 */
class LobbyViewModel(
    private val lobbyService: RealLobbyService
) : ViewModel() {

    private val _type = MutableStateFlow(true)
    val type = _type.asStateFlow()

    private val _game = MutableStateFlow<UUID?>(null)
    val game = _game.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun chooseGameType() {
        _type.value = _type.value.not()
    }

    fun joinGame() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _game.value = lobbyService.joinOrStartMatch(_type.value)
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
                val errorMessage = e.toString().split(": ").last()
                _error.value = errorMessage
            }
        }
    }

    fun resetError() {
        _error.value = null
    }

}
