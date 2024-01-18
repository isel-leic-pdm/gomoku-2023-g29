package ipl.isel.daw.gomoku.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun SquareImageView(
    modifier: Modifier = Modifier,
    image: Int,
    selected: Boolean = false,
    highlight: Boolean = false,
    onClick: () -> Unit = { },
) = Box(
    modifier = modifier
        .then(
            if (selected)
                Modifier.border(width = 1.dp, color = Color.Red)
            else
                Modifier //.border(BorderStroke(1.dp, Color.Black), shape = RectangleShape)
        )
        .clickable(true) { onClick() }
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = image.toString(),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    )
    if (highlight) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0x7700FF00),
                center = Offset(x = size.width / 2, y = size.height / 2),
                radius = (size.minDimension / 2) * 0.7F
            )
        }
    }
}

