package ipl.isel.daw.gomoku.login.model

import java.util.UUID


data class UserInfo(val username: String, val bearer: String, val userId: UUID) {
    init {
        require(validateUserInfoParts(username, bearer))
    }
}

fun userInfoOrNull(username: String, bearer: String, userId: UUID ): UserInfo? =
    if (validateUserInfoParts(username, bearer))
        UserInfo(username, bearer,  userId)
    else
        null

fun validateUserInfoParts(username: String, bearer: String) =
    (username.isNotBlank() && bearer.isNotBlank() && bearer != "null")
