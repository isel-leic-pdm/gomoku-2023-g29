package ipl.isel.daw.gomoku.lobby

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ipl.isel.daw.gomoku.DependenciesContainer
import ipl.isel.daw.gomoku.game.GameActivity
import ipl.isel.daw.gomoku.game.model.Turn
import ipl.isel.daw.gomoku.lobby.model.LobbyViewModel
import ipl.isel.daw.gomoku.lobby.model.RealLobbyService
import ipl.isel.daw.gomoku.lobby.ui.LobbyState
import ipl.isel.daw.gomoku.lobby.ui.LobbyView
import ipl.isel.daw.gomoku.utils.viewModelInit
import kotlinx.parcelize.Parcelize
import okhttp3.OkHttpClient
import java.util.UUID

class LobbyActivity : ComponentActivity() {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            //.cache(Cache(directory = cacheDir, maxSize = 50 * 1024 * 1024))
            .build()
    }

    private val jsonEncoder: Gson by lazy {
        GsonBuilder()
            .create()
    }

    private val repo by lazy {
        (application as DependenciesContainer)
    }

    private val viewModel by viewModels<LobbyViewModel> {
        viewModelInit {
            require(repo.userInfoRepo.userInfo != null)
            LobbyViewModel(RealLobbyService(repo.userInfoRepo.userInfo!!, httpClient, jsonEncoder))
        }
    }

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, LobbyActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val context = this
/*        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                // refresh the game state
                val game = viewModel.game.value
                if(game!=null) {
                    val info = MatchInfo(
                        game.id,
                        game.userId1,
                        game.userId2,
                        whoAmI = if (game.userId1 == repo.userInfoRepo.userInfo!!.userId)
                            Turn.PLAYER1.name else Turn.PLAYER2.name
                    )
                    GameActivity.navigate(context, info)
                }
                // delay the next refresh
                delay(POLLING_INTERVAL_MILLISECONDS)
            }
        }*/

        setContent {
            val error by viewModel.error.collectAsState()
            val traditional by viewModel.type.collectAsState()
            val game by viewModel.game.collectAsState()
            LobbyView(
                state = LobbyState(game?.id, traditional, error),
                onChangeMode = { viewModel.chooseGameType() },
                onStartOrJoinGame = {
                    val job = viewModel.joinGame()
                    while(!job.isCompleted || viewModel.isLoading.value || viewModel.game.value == null);
                    val game2 = viewModel.game.value
                    val info = MatchInfo(
                        game2!!.id,
                        game2.userId1,
                        game2.userId2,
                        whoAmI = if (game2.userId1 == repo.userInfoRepo.userInfo!!.userId)
                            Turn.PLAYER1.name else Turn.PLAYER2.name
                    )
                    GameActivity.navigate(this, info)

                    viewModel.resetError()
                },
                onBackRequest = { finish() },
                onErrorReset = { viewModel.resetError() },
            )
        }

/*        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchRooms()
            }
        }*/
    }
}

@Parcelize
data class MatchInfo (
    val uuid: UUID,
    val player1: UUID,
    var player2: UUID,
    val whoAmI: String
    ) : Parcelable



