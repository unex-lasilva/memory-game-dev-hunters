package br.mangarosa.memorygame.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme
import br.mangarosa.memorygame.classes.Player

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryGameTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        val a = intent.getSerializableExtra("player1") as? Player

                        @Suppress("DEPRECATION")
                        Text(text = a.toString())

                        @Suppress("DEPRECATION")
                        Text(text = intent.getSerializableExtra("player2").toString())

                        @Suppress("DEPRECATION")
                        Text(text = intent.getSerializableExtra("cardBoard").toString())
                    }
                }
            }
        }
    }
}