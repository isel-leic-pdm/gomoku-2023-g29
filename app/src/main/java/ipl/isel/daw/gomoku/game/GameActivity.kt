package ipl.isel.daw.gomoku.game

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ipl.isel.daw.gomoku.DependenciesContainer
import ipl.isel.daw.gomoku.game.model.GameViewModel
import ipl.isel.daw.gomoku.game.model.Piece
import ipl.isel.daw.gomoku.game.model.RealPlayService
import ipl.isel.daw.gomoku.game.ui.GameView
import ipl.isel.daw.gomoku.lobby.MatchInfo
import ipl.isel.daw.gomoku.utils.viewModelInit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

const val BOARD_SIDE = 8
val SQUARE_SIDE: Dp = 50.dp
val HEADER_THICKNESS: Dp = 20.dp
const val POLLING_INTERVAL_MILLISECONDS: Long = 1000 // 1 Second

class GameActivity : ComponentActivity() {

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

    companion object {
        const val MATCH_INFO_EXTRA = "MATCH_INFO_EXTRA"
        fun navigate(origin: Context, matchInfo: MatchInfo) {
            with(origin) {
                startActivity(
                    Intent(this, GameActivity::class.java).also {
                        it.putExtra(MATCH_INFO_EXTRA, matchInfo)
                    }
                )
            }
        }
    }

    @Suppress("DEPRECATION")
    private val info: MatchInfo by lazy {
         val info =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(MATCH_INFO_EXTRA, MatchInfo::class.java)
            else
                intent.getParcelableExtra(MATCH_INFO_EXTRA)

        checkNotNull(info)
    }

    private val viewModel by viewModels<GameViewModel> {
        viewModelInit {
            require(repo.userInfoRepo.userInfo != null)

            GameViewModel(
                RealPlayService(
                    repo.userInfoRepo.userInfo!!,
                    httpClient,
                    jsonEncoder
                ),
                info
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                // refresh the game state
                viewModel.refreshGame()
                // delay the next refresh
                delay(POLLING_INTERVAL_MILLISECONDS)
            }
        }

        setContent {
            val processedInfo by viewModel.processedInfo.collectAsState()
            val currentGame by viewModel.currentGame.collectAsState()
            val currentlyPlacing by viewModel.currentlyPlacing.collectAsState()
            val placedPieces by viewModel.placedGoPieces.collectAsState()
            val board by viewModel.playerBoard.collectAsState()
            val hit by viewModel.hit.collectAsState()
            val error by viewModel.error.collectAsState()

            GameView(
                onLeaveRequest = {
                    viewModel.handleForfeit()
                    finish()
                },
                info = processedInfo,
                currentGame = currentGame,
                playerBoard = board,
                currentlyPlacing = currentlyPlacing,
                placedGoPieces = placedPieces,
                myTurn = { viewModel.isMyTurn() },
                placePiece = { viewModel.placePiece() },
                //selectPiece = { piece -> viewModel.selectPiece(piece) },
                canPlaceCurrentPiece = { piece: Piece, l: Int, c :Int ->
                    viewModel.canPlaceCurrentPiece(
                        piece,
                        l, c
                    )
                },
                makeMove = { shots: Pair<Int,Int> -> viewModel.makeMove(shots) },
                //hit = hit,
                // gameStart = { viewModel.gameStart() },
                error = error,
                onErrorReset = { viewModel.resetError() },
                resetBoard = { viewModel.resetBoard() }
            )
        }
    }
}
