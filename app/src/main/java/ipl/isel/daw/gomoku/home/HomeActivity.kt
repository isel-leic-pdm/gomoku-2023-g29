package ipl.isel.daw.gomoku.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import ipl.isel.daw.gomoku.about.AboutActivity
import ipl.isel.daw.gomoku.leaderboard.LeaderboardActivity
import ipl.isel.daw.gomoku.lobby.LobbyActivity
import ipl.isel.daw.gomoku.DependenciesContainer
import ipl.isel.daw.gomoku.home.HomeScreenState
import ipl.isel.daw.gomoku.home.HomeView
import ipl.isel.daw.gomoku.login.LoginActivity
import ipl.isel.daw.gomoku.me.MeActivity
import ipl.isel.daw.gomoku.utils.viewModelInit

class HomeActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer)
            .userInfoRepo
    }

    private val viewModel: HomeViewModel by viewModels {
        viewModelInit {
            HomeViewModel(repo)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loggedState by viewModel.isLoggedIn.collectAsState()
            HomeView(
                state = HomeScreenState(null, loggedState),
                onLogoutRequest = {
                    if (loggedState) {
                        repo.userInfo = null
                        viewModel.logout()
                        LoginActivity.navigate(this)
                    }
                },
                onMeRequest = {
                    if (loggedState)
                        MeActivity.navigate(this)
                    else
                        LoginActivity.navigate(this)
                },
                onFindGameRequest = {
                    if (loggedState)
                        LobbyActivity.navigate(this)
                    else
                        LoginActivity.navigate(this)
                },
                onLeaderboardRequest = { LeaderboardActivity.navigate(this) },
                onInfoRequest = { AboutActivity.navigate(this) },
                onSignInOrSignUpRequest = {
                    if (!loggedState)
                        LoginActivity.navigate(this)
                },
                onExitRequest = { finishAndRemoveTask() }
            )

            lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    if (repo.userInfo != null) {
                        viewModel.login()
                    }
                }
            })
        }
    }
}

