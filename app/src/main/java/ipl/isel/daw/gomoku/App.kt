package ipl.isel.daw.gomoku

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val TRADITIONAL = 1
const val EXPANDED = 0
const val UNALTERED = -1

@Preview(showSystemUi = true)
@Composable
fun App() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        val isTraditional = rememberSaveable { mutableIntStateOf(UNALTERED) }
        val connectionType = rememberSaveable { mutableIntStateOf(UNALTERED) }

        if (isTraditional.intValue != UNALTERED) {
            if (connectionType.intValue != UNALTERED)
                GenerateBoard(isTraditional.intValue)
            else
                ChoosePlayType(connectionType)
        } else {
            ChooseGameType(isTraditional)
        }
    }
}


val menuModifier = Modifier
    .fillMaxSize()
    .paddingFrom(FirstBaseline, 200.dp, 0.dp)

@Composable
fun Menu() =
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = menuModifier
    ) {
        Text(text = "GOMOKU", fontSize = 30.sp)
        Text(text = "Choose Game Type: ", fontSize = 15.sp)
        Row {
            TextButton(onClick = { }) {
                Text(text = "Traditional")
            }
            TextButton(onClick = { }) {
                Text(text = "Expanded")
            }
        }
    }


@Composable
fun ChoosePlayType(connectionType: MutableIntState) =
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = menuModifier
    ) {
        Text(text = "GOMOKU", fontSize = 30.sp)
        Text(text = "Choose Connection Type: ", fontSize = 15.sp)
        Row {
            TextButton(onClick = {
                connectionType.intValue = TRADITIONAL
            }) {            // "Traditional" to spare "resources" (replication of values
                Text(text = "Same Device")
            }
            TextButton(onClick = { connectionType.intValue = EXPANDED }) {
                Text(text = "Wireless")
            }
        }
    }

@Composable
fun ChooseGameType(gamemode: MutableIntState) =
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = menuModifier
    ) {
        Text(text = "GOMOKU", fontSize = 30.sp)
        Row {
            TextButton(onClick = { gamemode.intValue = TRADITIONAL }) {
                Text(text = "Traditional")
            }
            TextButton(onClick = { gamemode.intValue = EXPANDED }) {
                Text(text = "Expanded")
            }
        }
    }


@Composable
fun GenerateBoard(isTraditional: Int) {
    val model = remember(GomokuModel(if(isTraditional == TRADITIONAL) 15 else 19))
    GomokuView(model = model, modifier = Modifier)

    /*
    val config = LocalConfiguration.current
    val height = config.screenHeightDp
    val width = config.screenWidthDp

    val boardSize = if (isTraditional == TRADITIONAL) 15 else 19
    val widthBox = width / boardSize
    val heightBox = height / boardSize
    val size = remember(if (widthBox < heightBox) widthBox else heightBox)


     */


    //BuildGrid(boardSize, size.dp)
    /*
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .size(if (widthBox < heightBox) widthBox.dp else heightBox.dp)
                .border(
                    width = 2.dp,
                    shape = ShapeDefaults.ExtraSmall,
                    color = Color(0xFFFF00FF)
                )
                .clickable(onClick = { placeOfPlay(text) })
        ) {
            Text(text = text.value, fontSize = 13.sp)
        }
     */
}

@Composable
private fun BuildGrid(boardSize: Int, size: Dp) {
    /** Table of Grid with remembered cell positions**/
    val placements = mutableListOf<MutableList<MutableState<Boolean>>>()

    Column {
        for (j: Int in 0 until boardSize) {
            placements.add(j, mutableListOf())
            Row {
                for (i: Int in 0 until boardSize) {
                    placements[j].add(i, rememberSaveable { mutableStateOf(false) })
                    MakeCell(size, placements, j, i)
                }
            }
        }
    }
}

@Composable
private fun MakeCell(
    size: Dp,
    placements: MutableList<MutableList<MutableState<Boolean>>>,
    j: Int,
    i: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .requiredSize(size)
            .size(size)
            //.border(width = 2.dp,color = MaterialTheme.colorScheme.error)
            .noRippleClickable(onClick = { placements[j][i].value = true })
    ) {
        if (placements[j][i].value)
            Image(
                painter = painterResource(id = R.drawable.sonic_piece),
                contentDescription = "sonic_piece"
            )
        else
            Image(
                painter = painterResource(id = R.drawable.grid),
                contentDescription = "grid"
            )
    }
}


/** Utility functions */
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@Composable
fun <T> remember(something: T): T {
    val anything by remember { mutableStateOf(something) }
    return anything
}