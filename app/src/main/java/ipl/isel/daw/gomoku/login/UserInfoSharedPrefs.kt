package ipl.isel.daw.gomoku.login

import android.content.Context
import ipl.isel.daw.gomoku.login.model.UserInfo
import ipl.isel.daw.gomoku.login.model.UserInfoRepository
import java.util.UUID

/**
 * A user information repository implementation supported in shared preferences
 */
class UserInfoSharedPrefs(private val context: Context): UserInfoRepository {

    private val userUsernameKey = "Username"
    private val userBearerKey = "Bearer"
    private val userIdKey = "Id"

    private val prefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }

    override var userInfo: UserInfo?
        get() {
            val savedUsername = prefs.getString(userUsernameKey, null)
            val savedBearer = prefs.getString(userBearerKey,null)
            val savedId = UUID.fromString(prefs.getString(userIdKey,null))
            return if (savedUsername != null && savedBearer != null)
                UserInfo(savedUsername, savedBearer, savedId)
            else
                null
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(userUsernameKey)
                    .remove(userBearerKey)
                    .remove(userIdKey)
                    .apply()
            else
                prefs.edit()
                    .putString(userUsernameKey, value.username)
                    .putString(userBearerKey, value.bearer)
                    .putString(userIdKey, value.userId.toString())
                    .apply()
        }
}