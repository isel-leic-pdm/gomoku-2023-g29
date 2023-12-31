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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipl.isel.daw.gomoku.R
import ipl.isel.daw.gomoku.TAG
import ipl.isel.daw.gomoku.game.BOARD_SIDE
import ipl.isel.daw.gomoku.game.model.Board
import ipl.isel.daw.gomoku.game.model.Loader
import ipl.isel.daw.gomoku.game.model.Piece
import ipl.isel.daw.gomoku.game.model.StateGame
import ipl.isel.daw.gomoku.game.model.indexToCoordinates
import ipl.isel.daw.gomoku.lobby.MatchInfo
import ipl.isel.daw.gomoku.lobby.ui.LobbyScreenTag
import ipl.isel.daw.gomoku.ui.SquareImageView
import ipl.isel.daw.gomoku.ui.TopBar
import ipl.isel.daw.gomoku.ui.theme.GomokuAndroidTheme
import java.util.UUID


@Composable
fun GameView(
    onLeaveRequest: () -> Unit,  //Semelhante ao onBackRequest, mas efetua uma leaveMatch, pode-se usar onBackRequested alternativamente
    info: MatchInfo,
    currentGame: StateGame,
    playerBoard: Board,
    currentlyPlacing: Piece?,
    placedGoPieces: ArrayList<Piece>,
    myTurn: () -> Boolean,
    placePiece: () -> Unit,
    // selectPiece: (Char) -> Unit,
    canPlaceCurrentPiece: (p: Piece, l: Int, c: Int) -> Boolean,
    // placements: () -> Array<Piece>,
    makeMove: (Pair<Int, Int>) -> Unit,
    // gameStart: () -> Unit,
    error: String?,
    onErrorReset: () -> Unit,
    resetBoard: () -> Unit
) {

    val view = rememberSaveable { mutableStateOf(false) }
    val readyButtonClicked = rememberSaveable { mutableStateOf(false) }

    var disabledButtonIndex: Int? = null
    val playerName = if (info.whoAmI == "PLAYER1") info.player1 else info.player2
    val enemyName = if (info.whoAmI == "PLAYER1") info.player2 else info.player1


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
                        GameEndedView(currentGame, info, onLeaveRequest)
                    }

                    Loader.STARTED -> {
                        GameStartedView(
                            playerName,
                            playerBoard,
                            currentlyPlacing,
                            canPlaceCurrentPiece,
                            placePiece,
                            currentGame,
                            myTurn,
                            makeMove,
                            enemyName,
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
    playerName: UUID, //TODO its a name
    playerBoard: Board,
    currentlyPlacing: Piece?,
    canPlaceCurrentPiece: (piece: Piece, l: Int, c: Int) -> Boolean,
    placePiece: () -> Unit,
    currentGame: StateGame,
    myTurn: () -> Boolean,
    makeMove: (Pair<Int,Int>) -> Unit,
    enemyName: UUID, //TODO its a name
) {
    Text(
        text = stringResource(id = R.string.game_game),
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.primaryVariant
    )

    Text(
        text = stringResource(id = R.string.game_playerboard) + " $playerName",
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.error
    )
    BoardView(
        board = playerBoard.cells,
        currentlyPlacing = currentlyPlacing,
        canPlaceCurrentPiece = canPlaceCurrentPiece,
        gameState = currentGame.gameState,
        placePiece = placePiece,
        myTurn = myTurn,
        makeMove = makeMove
    )

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
            text = "...$enemyName " + stringResource(id = R.string.game_enemyturn),
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
    info: MatchInfo,
    onLeaveRequest: () -> Unit
) {
    val winner = if (currentGame.result == 1) info.player1 else info.player2

    Text(
        text = "$winner " + stringResource(id = R.string.game_won),
        style = MaterialTheme.typography.h4,
        color = MaterialTheme.colors.primaryVariant
    )
    Button(onClick = { onLeaveRequest() }) { Text(text = stringResource(id = R.string.game_returntolobby)) }
}


@Composable
fun BoardView(
    board: Array<Piece>,
    currentlyPlacing: Piece?,
    canPlaceCurrentPiece: (piece: Piece, l: Int, c: Int) -> Boolean,
    placePiece: () -> Unit,
    gameState: Loader?,
    myTurn: () -> Boolean,
    makeMove: (Pair<Int, Int>) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.Start) {
            for (i in 0..7) {
                Row() {
                    for (j in 0..7) {
                        SquareImageView(
                            image = board[i * BOARD_SIDE + j].type.toImage(),
                            onClick = {
                                val p = indexToCoordinates(i * BOARD_SIDE + j)
                                if (currentlyPlacing != null && canPlaceCurrentPiece(
                                        currentlyPlacing,
                                        p.first, p.second
                                    )
                                ) placePiece()
                                else if (gameState == Loader.STARTED && myTurn())
                                    makeMove(
                                        Pair(p.first, p.second)
                                    ) //if true hitIncrease
                            }
                        )
                    }
                }
            }
        }
    }
}


/**
 *
 * ------------------------------- PREVIEWS-----------------------------------
 *
 */

@Preview(showBackground = true)
@Composable
private fun GamePreviewPlayingMyTurn() {
    val boardTest = "__222___1111___22____111____121___212_____1122__222111___22121__"
    val builtBoard = Board(boardTest)
    GameView(
        onLeaveRequest = {},  //Semelhante ao onBackRequest, mas efetua uma leaveMatch, pode-se usar onBackRequested alternativamente
        info = MatchInfo(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "PLAYER1"),
        currentGame = StateGame(Loader.STARTED, "", "", 0),
        playerBoard = builtBoard,
        currentlyPlacing = null,
        placedGoPieces = arrayListOf(),
        myTurn = { true },
        canPlaceCurrentPiece = { _,_, _ -> true },
        placePiece = {},
        //placements = { emptyArray() },
        makeMove = {},
        error = null,
        onErrorReset = {},
        resetBoard = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun GamePreviewPlayingEnemyTurn() {
    val boardTest = "__222___1111___22____111____121___212_____1122__222111___22121__"
    val builtBoard = Board(boardTest)
    GameView(
        onLeaveRequest = {},  //Semelhante ao onBackRequest, mas efetua uma leaveMatch, pode-se usar onBackRequested alternativamente
        info = MatchInfo(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "PLAYER1"),
        currentGame = StateGame(Loader.STARTED, "", "", 0),
        playerBoard = builtBoard,
        currentlyPlacing = null,
        placedGoPieces = arrayListOf(),
        myTurn = { false },
        canPlaceCurrentPiece = { _,_, _ -> true },
        placePiece = {},
        //placements = { emptyArray() },
        makeMove = {},
        error = null,
        onErrorReset = {},
        resetBoard = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun BoardPreview() {
    val boardTest = "__222___1111___22____111____121___212_____1122__222111___22121__"
    val builtBoard = Board(boardTest).cells
    BoardView(
        board = builtBoard,
        currentlyPlacing = null,
        canPlaceCurrentPiece = { _, _, _ -> true },
        placePiece = {},
        gameState = null,
        myTurn = { true },
        makeMove = {}
    )
}
