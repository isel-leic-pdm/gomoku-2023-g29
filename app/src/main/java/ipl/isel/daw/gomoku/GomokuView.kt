package ipl.isel.daw.gomoku

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
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

    val board = model.board
    Column(modifier = modifier) {
        for (x in board.indices) {
            Row {
                for (y in board[x].indices) {
                    model.board[x][y] = remember(something = 0)
                    Cell(size, model,x,y )
                }
            }
        }
    }
}

fun play(model: GomokuModel, x: Int, y: Int): Boolean {
    return model.play(x,y)
}

@Composable
fun Cell(size: Int, model: GomokuModel, x: Int, y: Int) {
    Box(
        Modifier
            .requiredSize(size.dp)
            .clickable(onClick = { model.play(x, y) }),
        contentAlignment = Alignment.Center,
    ) {
        when (model.board[x][y]) {
            0 -> Image(painterResource(R.drawable.grid), "grid")
            1 -> Image(painterResource(R.drawable.knuckles_piece), "2nd_player")
            2 -> Image(painterResource(R.drawable.sonic_piece), "1st_player")
        }
    }
}