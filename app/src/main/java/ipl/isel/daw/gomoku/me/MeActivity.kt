package ipl.isel.daw.gomoku.me

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ipl.isel.daw.gomoku.DependenciesContainer
import ipl.isel.daw.gomoku.me.model.MeViewModel
import ipl.isel.daw.gomoku.me.model.RealMeService
import ipl.isel.daw.gomoku.me.ui.MeScreenState
import ipl.isel.daw.gomoku.me.ui.MeView
import ipl.isel.daw.gomoku.utils.viewModelInit
import okhttp3.OkHttpClient

class MeActivity : ComponentActivity() {

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

    private val viewModel: MeViewModel by viewModels {
        viewModelInit {
            require(repo.userInfoRepo.userInfo != null)
            MeViewModel(
                RealMeService(repo.userInfoRepo.userInfo!!, httpClient, jsonEncoder)
            )
        }
    }

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, MeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (repo.userInfoRepo.userInfo == null) throw IllegalStateException()
        viewModel.fetchAllInfo()
        setContent {
            val error by viewModel.error.collectAsState()
            val myInfo by viewModel.myInfo.collectAsState()
            val myGamesHistory by viewModel.myGamesHistory.collectAsState()
            MeView(
                state = MeScreenState(
                    myInfo,
                    myGamesHistory,
                    error,
                ),
                onBackRequest = { finish() },
                onErrorReset = { viewModel.resetError()},
            )
        }
    }
}

