package ipl.isel.daw.gomoku.lobby.model


import com.google.gson.Gson
import ipl.isel.daw.gomoku.HOST
import ipl.isel.daw.gomoku.game.model.GameIdModel
import ipl.isel.daw.gomoku.login.model.UserInfo
import ipl.isel.daw.gomoku.utils.getClient
import ipl.isel.daw.gomoku.utils.handleResponse
import ipl.isel.daw.gomoku.utils.hypermedia.ApplicationJsonType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

interface LobbyService {

    suspend fun joinOrStartMatch(traditional: Boolean?): UUID

}

class RealLobbyService(
    private val userInfo: UserInfo,
    httpClient: OkHttpClient,
    private val jsonEncoder: Gson
): LobbyService {

    private val client = getClient(httpClient, userInfo.bearer)

    override suspend fun joinOrStartMatch(traditional: Boolean?): UUID {
        val body = (jsonEncoder.toJson(
            mapOf(
                "userId" to userInfo.userId,
                "traditional" to traditional
            )
        )).toRequestBody(ApplicationJsonType)

        val request = Request.Builder()
            .url("$HOST/games/start")
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            return handleResponse<GameIdModel>(response, jsonEncoder).gameId
        }
    }
}
