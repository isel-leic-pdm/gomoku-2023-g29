package ipl.isel.daw.gomoku.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipl.isel.daw.gomoku.login.model.UserInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(userRepo: UserInfoRepository) : ViewModel() {


    private val _isLoggedIn = MutableStateFlow<Boolean>(userRepo.userInfo != null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun login() {
        viewModelScope.launch {
            _isLoggedIn.value = true
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoggedIn.value = false
        }
    }
}