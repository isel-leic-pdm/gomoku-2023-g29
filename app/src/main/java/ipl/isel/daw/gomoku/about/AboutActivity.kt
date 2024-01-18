package ipl.isel.daw.gomoku.about

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ipl.isel.daw.gomoku.R
import ipl.isel.daw.gomoku.about.model.Author
import ipl.isel.daw.gomoku.about.ui.AboutView

class AboutActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutView(
                onBackRequest = { finish() },
                onSendEmailRequested =  ::onOpenSendEmail,
                onUrlRequested = ::openUrl,
                listOfAuthor = listOfAuthor
            )
        }
    }



    private fun onOpenSendEmail(email: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, "About the Gomoku App")
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(ContentValues.TAG, "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    private fun openUrl(url: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("Gomoku", "Failed to open URL", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }
}

private val listOfAuthor: List<Author> = listOf(
    Author("Douglas Incáo", "A47269@alunos.isel.pt", "https://github.com/Jorgulas"),
    Author("Maria do Rosário", "A46042@alunos.isel.pt", "https://github.com/rosariomachado"),
    Author("João Nunes", "A49475@alunos.isel.pt", "https://github.com/JoaoNunes49475")
)









