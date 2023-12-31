package ipl.isel.daw.gomoku.leaderboard.model

import com.google.gson.Gson
import ipl.isel.daw.gomoku.HOST
import ipl.isel.daw.gomoku.utils.handleResponse
import okhttp3.OkHttpClient
import okhttp3.Request

interface LeaderboardService {

    suspend fun getPlayerInfo(name: String): PlayerInfo

    suspend fun rankings(): List<PlayerInfo>
}

class RealLeaderboardService(
    private val httpClient: OkHttpClient,
    private val jsonEncoder: Gson
) : LeaderboardService {

    override suspend fun getPlayerInfo(name: String): PlayerInfo {
        val request = Request.Builder()
            .url("$HOST/users/rank/$name")
            .get()
            .build()
        return httpClient.newCall(request).execute().use { response ->
            handleResponse<PlayerInfo>(response, jsonEncoder)
        }
    }

    data class TopTen(val topTen :List<PlayerInfo>)

    override suspend fun rankings(): List<PlayerInfo> {
        val request = Request.Builder()
            .url("$HOST/users/top10")
            .get()
            .build()
        httpClient.newCall(request).execute().use { response ->
            return handleResponse<TopTen>(response, jsonEncoder).topTen.map{it}
        }
    }
}