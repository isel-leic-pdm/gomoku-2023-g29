package ipl.isel.daw.gomoku.game.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.PanToolAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipl.isel.daw.gomoku.R
import ipl.isel.daw.gomoku.TAG
import ipl.isel.daw.gomoku.game.model.Board
import ipl.isel.daw.gomoku.game.model.Loader
import ipl.isel.daw.gomoku.game.model.Piece
import ipl.isel.daw.gomoku.game.model.StateGame
import ipl.isel.daw.gomoku.game.model.Turn
import ipl.isel.daw.gomoku.game.model.Type
import ipl.isel.daw.gomoku.game.model.indexToCoordinates
import ipl.isel.daw.gomoku.lobby.MatchInfo
import ipl.isel.daw.gomoku.lobby.ui.LobbyScreenTag
import ipl.isel.daw.gomoku.ui.SquareImageView
import ipl.isel.daw.gomoku.ui.TopBar
import ipl.isel.daw.gomoku.ui.theme.GomokuAndroidTheme
import java.util.UUID


@Composable
fun GameView(
    // Semelhante ao onBackRequest, mas efetua uma leaveMatch, pode-se usar onBackRequested alternativamente
    onLeaveRequest: () -> Unit,
    info: MatchInfo,
    currentGame: StateGame,
    playerBoard: Board?,
    myTurn: () -> Boolean,
    makeMove: (Pair<Int, Int>) -> Unit,
    error: String?,
    onErrorReset: () -> Unit,
) {

    val view = rememberSaveable { mutableStateOf(false) }

    GomokuAndroidTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LobbyScreenTag),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                if (currentGame.gameState != Loader.ENDED) {
                    TopBar(
                        onLeaveRequested = { onLeaveRequest() },
                        onInspectRequested = {
                            view.value = !view.value
                        } //Para inspecionar a própria Board
                    )
                }
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Log.v(TAG, currentGame.toString())
                if (error != null) {
                    Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
                    onErrorReset()
                }
                when (currentGame.gameState) {
                    Loader.WAITING -> {
                        GameWaitingView()
                    }

                    Loader.ENDED -> {
                        GameEndedView(currentGame, onLeaveRequest)
                    }

                    Loader.STARTED -> {
                        GameStartedView(
                            playerBoard = playerBoard,
                            makeMove = makeMove,
                            currentGame = currentGame,
                            myTurn = myTurn,
                        )
                    }
                    else -> {
                        Text(
                            text = "It was supposed to show something...",
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.primaryVariant
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun GameStartedView(
    playerBoard: Board?,
    makeMove: (Pair<Int, Int>) -> Unit,
    currentGame: StateGame,
    myTurn: () -> Boolean,
) {
    Text(text = stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.h3,
        color = MaterialTheme.colors.primaryVariant,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily(Font(R.font.dancing_font))
        )

    val gameMode = if(playerBoard?.cells?.size?.rem(15) == 0)
        stringResource(id = R.string.traditional)
    else
        stringResource(id = R.string.renju)
    Log.v(TAG, "Game mode: $gameMode")
    Text(
        text = stringResource(id = R.string.mode) + " $gameMode",
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.primaryVariant,
        textAlign = TextAlign.Center,
    )
    
    if (playerBoard != null) {
        BoardView(
            board = playerBoard.cells,
            gameState = currentGame.gameState,
            myTurn = myTurn,
            makeMove = makeMove
        )
    }

    if (myTurn()) {
        Icon(
            Icons.Filled.PanToolAlt,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = stringResource(id = R.string.game_myturn),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primaryVariant
        )
    } else {
        Icon(
            Icons.Default.HourglassBottom,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = stringResource(id = R.string.game_enemyturn),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primaryVariant
        )
    }
}

@Composable
private fun GameWaitingView() {
    Text(
        text = stringResource(id = R.string.game_waitingforplayer),
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.primaryVariant
    )
}

@Composable
private fun GameEndedView(
    currentGame: StateGame,
    onLeaveRequest: () -> Unit
) {

    val winner = currentGame.result == 1

    Text(
        text = if(!winner) stringResource(id = R.string.game_lost) else stringResource(id = R.string.game_won),
        style = MaterialTheme.typography.h4,
        color = MaterialTheme.colors.primaryVariant
    )
    Button(onClick = { onLeaveRequest() }) { Text(text = stringResource(id = R.string.game_returntolobby)) }
}


@Composable
fun BoardView(
    board: Array<Piece>,
    gameState: Loader?,
    myTurn: () -> Boolean,
    makeMove: (Pair<Int, Int>) -> Unit
) {
    val config = LocalConfiguration.current
    val height = config.screenHeightDp
    val width = config.screenWidthDp
    val cellRatio = if( board.size % 15 == 0 ) 15 else 19

    val widthBox = width / cellRatio
    val heightBox = height / cellRatio
    val size = if (widthBox < heightBox) widthBox else heightBox
    Log.v(TAG, board.toStringToLog())

    Box(modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 0..<cellRatio) {
                Row {
                    for (j in 0..<cellRatio) {
                        SquareImageView(
                            modifier = Modifier.size(size.dp),
                            image = board[i * cellRatio + j].type.toImage(),
                            onClick = {
                                val p = indexToCoordinates(i * cellRatio + j)
                                Log.v(TAG, "Clicked on $p at $i,$j with ratio: ${i * cellRatio + j}")
                                if (gameState == Loader.STARTED && myTurn()) {
                                    makeMove(Pair(p.second, p.first)) //if true hitIncrease
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

fun <T> Array<T>.toStringToLog(): String {
    val str = StringBuilder()
    this.forEach { str.append(it.toString()) }
    return str.toString()
}


/**
 *
 * ------------------------------- PREVIEWS-----------------------------------
 *
 */

@Preview(showBackground = true)
@Composable
private fun GamePreviewPlayingMyTurn() {

    val builtBoard = Board(15*15)
    GameView(
        onLeaveRequest = {},  //Semelhante ao onBackRequest, mas efetua uma leaveMatch, pode-se usar onBackRequested alternativamente
        info = MatchInfo(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Turn.PLAYER1.name),
        currentGame = StateGame(Loader.STARTED, "", "", 0),
        playerBoard = builtBoard,
        myTurn = { true },
        makeMove = {},
        error = null,
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun GamePreviewPlayingEnemyTurn() {
    val builtBoard = Board(15*15)
    builtBoard.cells[0] = Piece(Type.PLAYER_1)
    builtBoard.cells[1] = Piece(Type.PLAYER_2)
    builtBoard.cells[15] = Piece(Type.PLAYER_1)
    builtBoard.cells[16] = Piece(Type.PLAYER_2)
    builtBoard.cells[30] = Piece(Type.PLAYER_1)
    GameView(
        onLeaveRequest = {},  //Semelhante ao onBackRequest, mas efetua uma leaveMatch, pode-se usar onBackRequested alternativamente
        info = MatchInfo(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Turn.PLAYER1.name),
        currentGame = StateGame(Loader.STARTED, "", "", 0),
        playerBoard = builtBoard,
        myTurn = { false },
        makeMove = {},
        error = null,
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun BoardPreview() {
    val builtBoard = Board(15*15).cells
    BoardView(
        board = builtBoard,
        gameState = null,
        myTurn = { true },
        makeMove = {}
    )
}
