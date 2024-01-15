package ipl.isel.daw.gomoku

import android.app.Application
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ipl.isel.daw.gomoku.about.model.AboutService
import ipl.isel.daw.gomoku.about.model.RealAboutService
import ipl.isel.daw.gomoku.leaderboard.model.LeaderboardService
import ipl.isel.daw.gomoku.leaderboard.model.RealLeaderboardService
import ipl.isel.daw.gomoku.login.UserInfoSharedPrefs
import ipl.isel.daw.gomoku.login.model.LoginService
import ipl.isel.daw.gomoku.login.model.RealLoginService
import ipl.isel.daw.gomoku.login.model.UserInfoRepository
import ipl.isel.daw.gomoku.utils.GomokuWorker
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


const val TAG = "GomokuApp"

const val HOST = "http://10.0.2.2:8080"


/**
 * The contract for the object that holds all the globally relevant dependencies.
 */
interface DependenciesContainer {
    val leaderboardService : LeaderboardService
    val loginService : LoginService
    val aboutService: AboutService
    val userInfoRepo : UserInfoRepository
}

/**
 * The application class to be used as a Service Locator.
 */
class GomokuApplication : DependenciesContainer, Application() {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            //.cache(Cache(directory = cacheDir, maxSize = 50 * 1024 * 1024))
            .build()
    }

    private val jsonEncoder: Gson by lazy {
        GsonBuilder()
            .create()
    }

    override val leaderboardService: LeaderboardService
        get() = RealLeaderboardService(httpClient,jsonEncoder)
    override val loginService: LoginService
        get() = RealLoginService(httpClient, jsonEncoder)
    override val aboutService: AboutService
        get() = RealAboutService(httpClient,jsonEncoder)
    override val userInfoRepo: UserInfoRepository
        get() = UserInfoSharedPrefs(this)

    private val workerConstraints  = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(true)
        .build()

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "GomokuApplication.onCreate() on process ${android.os.Process.myPid()}")

        val workRequest =
            PeriodicWorkRequestBuilder<GomokuWorker>(repeatInterval = 12, TimeUnit.HOURS)
                .setConstraints(workerConstraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "GomokuWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
        Log.v(TAG, "GomokuWorker was scheduled")
    }

}
