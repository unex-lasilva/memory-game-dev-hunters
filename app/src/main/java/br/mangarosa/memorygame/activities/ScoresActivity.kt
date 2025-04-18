package br.mangarosa.memorygame.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.mangarosa.memorygame.activities.components.GameLogo
import br.mangarosa.memorygame.activities.components.ScreenBackground
import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme

class ScoresActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryGameTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    Test()

                }
            }
        }
    }
}

@Composable
fun Test() {

    Box(modifier = Modifier.fillMaxSize()) {
        ScreenBackground()

        Box() {
            GameLogo()
            Box(contentAlignment = Alignment.BottomCenter) {}

        }



    }

}