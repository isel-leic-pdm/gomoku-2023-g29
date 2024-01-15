package ipl.isel.daw.gomoku.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Shield
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ipl.isel.daw.gomoku.R


const val NavigateBackTag = "NavigateBack"
const val LogoutTag = "LogoutBack"
const val ForfeitTag = "ForfeitTag"
const val NavigateInfoTag = "NavigateInfoTag"
const val InspectBoardTag = "InspectBoardTag"
//const val RefreshLobbyTag = "RefreshLobbyTag"

@Composable
fun TopBar(
    onBackRequested: (() -> Unit)? = null,
    onInfoRequested: (() -> Unit)? = null,
    onLogoutRequested: (() -> Unit)? = null,
    onLeaveRequested: (() -> Unit)? = null,
    onInspectRequested: (() -> Unit)? = null,
) {
    TopAppBar(
        title = { stringResource(id = R.string.app_name) },
        navigationIcon = {
            if (onBackRequested != null) {
                IconButton(onClick = onBackRequested, modifier = Modifier.testTag(NavigateBackTag)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
            if(onLogoutRequested != null){
                IconButton(onClick = {onLogoutRequested()}, modifier = Modifier.testTag(LogoutTag)){
                    Icon(Icons.Default.Logout,contentDescription = null)
                }
            }
            if(onLeaveRequested != null){
                IconButton(onClick = {onLeaveRequested()},modifier = Modifier.testTag(ForfeitTag)){
                    Icon(Icons.Default.Flag, contentDescription = null)
                }
            }
        },
        actions = {
            if (onInfoRequested != null) {
                IconButton(onClick = { onInfoRequested() },modifier = Modifier.testTag(
                    NavigateInfoTag
                )) {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
            }
            if (onInspectRequested != null) {
                IconButton(onClick = { onInspectRequested() },modifier = Modifier.testTag(
                    InspectBoardTag
                )) {
                    Icon(Icons.Filled.Shield, contentDescription = null)
                }
            }
        }
    )
}