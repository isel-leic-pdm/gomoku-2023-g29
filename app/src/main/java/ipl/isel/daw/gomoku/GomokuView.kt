package ipl.isel.daw.gomoku

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GomokuView(model: GomokuModel, modifier: Modifier) {
    val config = LocalConfiguration.current
    val height = config.screenHeightDp
    val width = config.screenWidthDp

    val widthBox = width / model.boardSize
    val heightBox = height / model.boardSize
    val size = if (widthBox < heightBox) widthBox else heightBox

    val conditions = GameConditions(
            model = model,
            gameState = rememberSaveable{ mutableStateOf(model.isFinished()) },
            turn = rememberSaveable{ mutableIntStateOf(model.getTurn()) }
        )

    Column(modifier = modifier) {
        for (x in model.board.indices) {
            Row {
                for (y in model.board[x].indices) {
                    model.board[x][y] = rememberSaveable { mutableIntStateOf(0) }
                    Cell(size, conditions,x,y)
                }
            }
        }
    }
}

fun play(gc: GameConditions, x: Int, y: Int): Boolean {
    return if(!gc.model.isFinished()) {
        if(gc.turn.value != gc.model.getTurn())
            gc.model.updateTurn(gc.turn.value)
        gc.model.play(x, y)
        gc.turn.value = gc.model.getTurn()
        true
    } else {
        gc.gameState.value = true
        false
    }
}

@Composable
fun Cell(size: Int, gc: GameConditions, x: Int, y: Int) {
    val model = gc.model
    Box(
        Modifier
            .requiredSize(size.dp)
            .noRippleClickable(
                onClick = { play(gc, x, y) },
                enabled = (model.board[x][y].intValue == 0
                        && !gc.gameState.value)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(painterResource(R.drawable.grid), "grid")
        when (model.board[x][y].intValue) {
            1 -> Image(painterResource(R.drawable.knuckles_piece), "2nd_player")
            2 -> Image(painterResource(R.drawable.sonic_piece), "1st_player")
        }
    }
}