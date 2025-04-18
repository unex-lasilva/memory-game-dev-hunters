package br.mangarosa.memorygame.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.mangarosa.memorygame.activities.components.MainLogo
import br.mangarosa.memorygame.activities.components.ScreenBackground
import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme
import kotlinx.coroutines.delay

private const val SEGS_SHOWING_SPLASH_SCREEN = 5
private const val SPLASH_SCREEN_LOGO_SIZE = 340

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryGameTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SplashScreen()
                    MainScreenDelayedCaller(this)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SplashScreen() {
    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ScreenBackground()
        GameLogo()
    }
}

@Composable
private fun GameLogo() {
    MainLogo(modifier = Modifier.size(SPLASH_SCREEN_LOGO_SIZE.dp))
}

@Composable
private fun MainScreenDelayedCaller(activity: SplashScreenActivity) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(SEGS_SHOWING_SPLASH_SCREEN * 1000L)
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        activity.finish()
    }
}