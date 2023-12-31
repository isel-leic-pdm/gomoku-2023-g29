package ipl.isel.daw.gomoku.me.model

import com.google.gson.Gson
import ipl.isel.daw.gomoku.leaderboard.model.PlayerInfo
import ipl.isel.daw.gomoku.login.model.UserInfo
import ipl.isel.daw.gomoku.HOST
import ipl.isel.daw.gomoku.utils.getClient
import ipl.isel.daw.gomoku.utils.handleResponse
import okhttp3.OkHttpClient
import okhttp3.Request

interface MeService {

    suspend fun getMyInfo(): PlayerInfo

    //suspend fun getMyGamesHistory(): List<GameDTO>
}

class RealMeService(
    private val userInfo: UserInfo,
    httpClient: OkHttpClient,
    private val jsonEncoder: Gson
) : MeService {

    private val client = getClient(httpClient, userInfo.bearer)

    override suspend fun getMyInfo(): PlayerInfo {
        val request = Request.Builder()
            .url("$HOST/users/rank/${userInfo.username}")
            .get()
            .build()
        client.newCall(request).execute().use { response ->
            return handleResponse<PlayerInfo>(response, jsonEncoder)
        }
    }

/*    override suspend fun getMyGamesHistory(): List<GameDTO> {
        val request = Request.Builder()
            .url("$HOST/me/history")
            .get()
            .build()
        client.newCall(request).execute().use { response ->
            return handleResponse<Array<GameDTO>>(response, jsonEncoder ).map{it}.reversed()
        }
    }*/


}