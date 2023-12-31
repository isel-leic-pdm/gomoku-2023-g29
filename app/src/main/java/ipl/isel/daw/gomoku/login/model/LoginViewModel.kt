package ipl.isel.daw.gomoku.login.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipl.isel.daw.gomoku.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class LoginViewModel(
    private val loginService: LoginService,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _userId = MutableStateFlow<UUID?>(null)
    val userId = _userId.asStateFlow()

    private val _token = MutableStateFlow<Token?>(null)
    val token = _token.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()


    fun fetchLoginToken(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                _token.value = loginService.login(username, password)
                _userId.value = loginService.getUserId(username)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                val errorMessage = e.toString().split(": ").last()
                _error.value = errorMessage
                _token.value = null
                _userId.value = null
            }
            _isLoading.value = false
        }
    }

    fun fetchRegisterToken(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                _token.value = loginService.register(username, password)
                _userId.value = loginService.getUserId(username)
            } catch (e: Exception) {
                Log.e(TAG, e.toString() + e.stackTraceToString())
                val errorMessage = e.toString().split(": ").last()
                _error.value = errorMessage
                _token.value = null
                _userId.value = null
            }
            _isLoading.value = false
        }
    }

    fun resetError(){
        _error.value = null
    }
}