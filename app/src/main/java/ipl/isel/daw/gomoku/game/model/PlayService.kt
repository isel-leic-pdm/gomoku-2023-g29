package ipl.isel.daw.gomoku.game.model

import com.google.gson.Gson
import ipl.isel.daw.gomoku.HOST
import ipl.isel.daw.gomoku.login.model.UserInfo
import ipl.isel.daw.gomoku.utils.handleResponse
import ipl.isel.daw.gomoku.utils.hypermedia.ApplicationJsonType
import ipl.isel.daw.gomoku.game.model.GameBoard
import ipl.isel.daw.gomoku.game.model.GameOutputModel
import ipl.isel.daw.gomoku.game.model.HitOrMiss
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import java.util.UUID

interface PlayService {

    val client: OkHttpClient

    suspend fun forfeitMatch(gameId: UUID)

    suspend fun refreshedGameState(gameId: UUID): GameOutputModel

    suspend fun makeMove(gameId: UUID, shotsCoordinates: Pair<Int,Int>): Boolean
}

class RealPlayService(
    private val userInfo: UserInfo,
    httpClient: OkHttpClient,
    private val jsonEncoder: Gson,
) : PlayService {
    /**
    This interceptor adds authorization bearer token header to every request regarding games API
     */
    override val client = httpClient.newBuilder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            val builder = originalRequest.newBuilder()
                .header("Authorization", "Bearer ${userInfo.bearer}")

            val newRequest = builder.build()
            chain.proceed(newRequest)
        }.build()

    override suspend fun forfeitMatch(gameId: UUID) {
        val request = Request.Builder()
            .url("$HOST/games/forfeit/$gameId")
            .post(EMPTY_REQUEST)
            .build()

        client.newCall(request).execute().use { response ->
            handleResponse<GameBoard>(response, jsonEncoder)
        }
    }

    override suspend fun refreshedGameState(gameId: UUID): GameOutputModel {
        val request = Request.Builder()
            .url("$HOST/games/id/$gameId")
            .build()

        client.newCall(request).execute().use { response ->
            return handleResponse<GameOutputModel>(response, jsonEncoder)
        }
    }

    override suspend fun makeMove(gameId: UUID, shotsCoordinates: Pair<Int,Int>): Boolean {
        val body = (jsonEncoder.toJson(
            mapOf(
                "userId" to userInfo.userId,
                "l" to shotsCoordinates.first,
                "c" to shotsCoordinates.second
            )
        )).toRequestBody(ApplicationJsonType)

        val request = Request.Builder()
            .url("$HOST/games/piece/$gameId")
            .put(body)
            .build()

        client.newCall(request).execute().use { response ->
            return handleResponse<HitOrMiss>(response, jsonEncoder)
                .hitOrMiss.first()
        }
    }
}