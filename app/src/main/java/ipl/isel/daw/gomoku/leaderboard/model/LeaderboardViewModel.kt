package ipl.isel.daw.gomoku.leaderboard.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipl.isel.daw.gomoku.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * View model for the Leaderboard Screen hosted by [LeaderboardActivity]
 */
class LeaderboardViewModel(
    val leaderboard: LeaderboardService,
) : ViewModel() {

    private val _rankings = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val rankings = _rankings.asStateFlow()

    private val _playerFound = MutableStateFlow<PlayerInfo?>(null)
    val playerFound = _playerFound.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()


    /** ----------------------------------------------------------------------- */

    fun fetchRankings() {
        viewModelScope.launch(Dispatchers.IO) {
            _rankings.value=
                try {
                    leaderboard.rankings()
                } catch (e: Exception) {
                    Log.v(TAG, e.toString())
                    val errorMessage = e.toString().split(": ").last()
                    _error.value = errorMessage
                    emptyList()
                }
        }
    }

    fun fetchPlayerInfo(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _playerFound.value =
                try {
                    leaderboard.getPlayerInfo(name)
                } catch (e: Exception) {
                    Log.v(TAG, e.toString())
                    val errorMessage = e.toString().split(": ").last()
                    _error.value = errorMessage
                    null
                }
        }
    }

    fun resetError() {
        _error.value = null
    }

}