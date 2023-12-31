package ipl.isel.daw.gomoku.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ipl.isel.daw.gomoku.login.model.LoginViewModel
import ipl.isel.daw.gomoku.login.model.Token
import ipl.isel.daw.gomoku.login.model.UserInfo
import ipl.isel.daw.gomoku.login.ui.LoginScreenState
import ipl.isel.daw.gomoku.login.ui.LoginView
import ipl.isel.daw.gomoku.DependenciesContainer
import ipl.isel.daw.gomoku.TAG
import ipl.isel.daw.gomoku.utils.viewModelInit
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID


class LoginActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer)
    }
    private val viewModel: LoginViewModel by viewModels {
        viewModelInit {
            LoginViewModel(repo.loginService)
        }
    }

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(
            TAG,
            "GomokuApplication Login Activity.onCreate() on process ${android.os.Process.myPid()}"
        )

        setContent {
            val loadingState by viewModel.isLoading.collectAsState()
            val token by viewModel.token.collectAsState()
            val error by viewModel.error.collectAsState()
            LoginView(
                state = LoginScreenState(token, loadingState,error),
                onSignupRequest = { username, password ->
                    viewModel.fetchRegisterToken(username, password)
                    runBlocking {
                        launch {
                            while (viewModel.isLoading.value);
                            val tok: Token? = viewModel.token.value
                            val userId: UUID? = viewModel.userId.value
                            if (tok != null && userId != null) {
                                repo.userInfoRepo.userInfo = UserInfo(username, tok.token, userId)
                                if (repo.userInfoRepo.userInfo != null)
                                    finish()
                            }
                        }
                    }
                    viewModel.resetError()
                },
                onSignInRequest = { username, password ->
                    viewModel.fetchLoginToken(username, password)
                    runBlocking {
                        launch {
                            while (viewModel.isLoading.value);
                            val tok: Token? = viewModel.token.value
                            val userId: UUID? = viewModel.userId.value
                            if (tok != null && userId != null) {
                                repo.userInfoRepo.userInfo = UserInfo(username, tok.token, userId)
                                if (repo.userInfoRepo.userInfo != null)
                                    finish()
                            }
                        }
                    }
                    viewModel.resetError()
                },
                onBackRequest = {
                    finish()
                }
            )
        }

    }

}
