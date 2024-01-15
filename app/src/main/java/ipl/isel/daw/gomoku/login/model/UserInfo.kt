package ipl.isel.daw.gomoku.login.model

import java.util.UUID


data class UserInfo(val username: String, val bearer: String, val userId: UUID) {
    init {
        require(validateUserInfoParts(username, bearer, userId))
    }
}

fun userInfoOrNull(username: String, bearer: String, userId: UUID ): UserInfo? =
    if (validateUserInfoParts(username, bearer, userId))
        UserInfo(username, bearer,  userId)
    else
        null

fun validateUserInfoParts(username: String, bearer: String, uuid: UUID) =
    (username.isNotBlank() && bearer.isNotBlank() && bearer != "null" && uuid != UUID(0,0))
