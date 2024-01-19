package ipl.isel.daw.gomoku.leaderboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ipl.isel.daw.gomoku.DependenciesContainer
import ipl.isel.daw.gomoku.leaderboard.model.LeaderboardViewModel
import ipl.isel.daw.gomoku.leaderboard.ui.LeaderboardState
import ipl.isel.daw.gomoku.leaderboard.ui.LeaderboardView
import ipl.isel.daw.gomoku.utils.viewModelInit


class LeaderboardActivity : ComponentActivity() {

    private val viewModel by viewModels<LeaderboardViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            LeaderboardViewModel(app.leaderboardService)
        }
    }

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, LeaderboardActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchRankings()
        setContent {
            val error by viewModel.error.collectAsState()
            val playerFound by viewModel.playerFound.collectAsState()
            val rankings by viewModel.rankings.collectAsState()
            LeaderboardView(
                state = LeaderboardState(
                    playerFound,
                    rankings,
                    error,
                ),
                onFindPlayer = { username -> viewModel.fetchPlayerInfo(username) },
                onBackRequest = { finish() },
                onErrorReset = {viewModel.resetError()},
            )
        }
    }
}
