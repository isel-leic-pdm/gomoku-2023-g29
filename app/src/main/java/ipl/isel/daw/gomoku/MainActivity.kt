package ipl.isel.daw.gomoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ipl.isel.daw.gomoku.ui.theme.GomokuTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GomokuTheme {
                // A surface container using the 'background' color from the theme
                App()
            }
        }
    }



}