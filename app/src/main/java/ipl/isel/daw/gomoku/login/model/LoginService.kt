package ipl.isel.daw.gomoku.login.model

import com.google.gson.Gson
import ipl.isel.daw.gomoku.HOST
import ipl.isel.daw.gomoku.utils.handleResponse
import ipl.isel.daw.gomoku.utils.hypermedia.ApplicationJsonType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

interface LoginService {

    suspend fun login(username: String, password: String): Token

    suspend fun register(username: String, password: String): Token

    suspend fun getUserId(username: String): UUID?
}

class RealLoginService(
    private val httpClient: OkHttpClient,
    private val jsonEncoder: Gson
) : LoginService {

    private fun bodyBuilder(name :String,pass :String) =
        ("{ \"username\": \"$name\",\"password\": \"$pass\" }").toRequestBody(ApplicationJsonType)

    override suspend fun login(username: String, password: String): Token {
        val body = bodyBuilder(username,password)
        val request = Request.Builder()
            .url("$HOST/users/login")
            .post(body)
            .build()
        httpClient.newCall(request).execute().use { response ->
            return handleResponse<Token>(response, jsonEncoder)
        }
    }

    override suspend fun register(username: String, password: String): Token {
        val body = bodyBuilder(username,password)
        val request = Request.Builder()
            .url("$HOST/users/createUser")
            .post(body)
            .build()
        httpClient.newCall(request).execute().use { response ->
            return handleResponse<Token>(response, jsonEncoder)
        }
    }

    override suspend fun getUserId(username: String): UUID {
        val request = Request.Builder()
            .url("$HOST/users/$username")
            .get()
            .build()
        class UserId(val id: UUID, val username: String)
        return httpClient.newCall(request).execute().use { response ->
            handleResponse<UserId>(response, jsonEncoder).id
        }

    }


}