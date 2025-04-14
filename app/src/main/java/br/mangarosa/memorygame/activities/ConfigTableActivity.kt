package br.mangarosa.memorygame.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.mangarosa.memorygame.R
import br.mangarosa.memorygame.activities.components.MainText
import br.mangarosa.memorygame.activities.components.ScreenBackground
import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme

val TABLE_OPTIONS = listOf("4 x 4", "6 x 6")

class ConfigTableActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryGameTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    ConfigScreen()
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConfigScreen() {
    Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        ScreenBackground()
        MainContent()
    }
}

@Composable
private fun MainContent() {
    Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Title()
        Options()
    }
}

@Composable
private fun Title() {
    MainText(text = "ESCOLHA UM TABULEIRO", fontSize = 52, modifier = Modifier.padding(10.dp))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Options() {
    val context = LocalContext.current
    FlowRow {
        TABLE_OPTIONS.forEach { Option(optionLabel = it, actionOnClick = {
            val intent = Intent(context, ConfigPlayersActivity::class.java).putExtra("tableSize", it)
            context.startActivity(intent)
        })}
    }
}

@Composable
private fun Option(optionLabel: String, actionOnClick: () -> Unit) {
    val modifiers = Modifier.padding(horizontal = 5.dp, vertical = 4.dp).clickable { actionOnClick() }
    Box (contentAlignment = Alignment.Center, modifier = modifiers) {
        OptionBase()
        OptionLabel(optionLabel)
    }
}

@Composable
private fun OptionLabel(optionLabel: String) {
    MainText(text = optionLabel, fontSize = 40)
}

@Composable
private fun OptionBase() {
    Image (
        painter = painterResource(id = R.drawable.table_option),
        contentDescription = "Base com textura de madeira para exibir a opção de tabuleiro",
    )
}