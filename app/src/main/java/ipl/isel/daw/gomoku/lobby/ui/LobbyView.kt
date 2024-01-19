package ipl.isel.daw.gomoku.lobby.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipl.isel.daw.gomoku.R
import ipl.isel.daw.gomoku.ui.TopBar
import ipl.isel.daw.gomoku.ui.theme.GomokuAndroidTheme
import java.util.UUID

const val GameInfoViewTag = "GameInfoView"
const val LobbyScreenTag = "LobbyScreenTag"

data class LobbyState(
    val game: UUID? = null,
    val traditional: Boolean = true,
    val error: String? = null,
)

@Composable
fun GameInfoView(
    traditional: Boolean,
    onGameSelected: () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onGameSelected() }
            .testTag(GameInfoViewTag)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.play) + " " +
                        if(traditional)
                            stringResource(id = R.string.traditional)
                        else stringResource(id = R.string.renju),
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CreateGameView(
    onGameModeToggle: (Boolean) -> Unit,
    isTraditional: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.mode) + " " + stringResource(id = R.string.traditional),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .padding(start = 8.dp, top = 13.dp, bottom = 16.dp),
        )
        RadioButton(
            selected = isTraditional,
            onClick = { onGameModeToggle(true) }
        )
        Text(
            text =  stringResource(id = R.string.renju),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .padding(start = 8.dp, top = 13.dp, bottom = 16.dp),
        )
        RadioButton(
            selected = !isTraditional,
            onClick = { onGameModeToggle(false) }
        )
    }
}


@Composable
fun LobbyView(
    state: LobbyState = LobbyState(),
    onStartOrJoinGame: () -> Unit,
    onBackRequest: () -> Unit,
    onErrorReset: () -> Unit,
    onChangeMode: () -> Unit,
    onWaitQueue: () -> Boolean
) {

    GomokuAndroidTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LobbyScreenTag),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(onBackRequested = { onBackRequest() })
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.lobby_lobby),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primaryVariant
                )
                CreateGameView(
                    onGameModeToggle = { onChangeMode() },
                    isTraditional = state.traditional,
                )
                Spacer(modifier = Modifier.height(16.dp))
                GameInfoView(
                    traditional = state.traditional,
                    onGameSelected = { onStartOrJoinGame() }
                )
                if(onWaitQueue())
                    Text(
                        text = stringResource(id = R.string.game_waitingforplayer),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primaryVariant
                    )
/*              
                GameInfoView(       // this will check if its traditional or not
                    traditional = state.traditional,
                    onGameSelected = { onStartOrJoinGame(state.game) }
                )
*/
                if (state.error != null) {
                    Toast.makeText(LocalContext.current, state.error, Toast.LENGTH_LONG).show()
                    onErrorReset()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GameInfoPreview() {
    GameInfoView(
        traditional = true,
        onGameSelected = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun CreateGamePreview() {
    CreateGameView(
        onGameModeToggle = { },
        isTraditional = true
    )
}

@Preview(showBackground = true)
@Composable
private fun LobbyPreview() {
    LobbyView(
        state = LobbyState(UUID.randomUUID()),
        onStartOrJoinGame = {},
        onBackRequest = {},
        onErrorReset = {},
        onChangeMode = {},
        { false }
    )
}
