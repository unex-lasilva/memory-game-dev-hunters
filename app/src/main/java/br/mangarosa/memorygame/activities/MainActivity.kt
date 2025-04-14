package br.mangarosa.memorygame.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.mangarosa.memorygame.R
import br.mangarosa.memorygame.activities.components.MainLogo
import br.mangarosa.memorygame.activities.components.ScreenBackground
import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme
import br.mangarosa.memorygame.activities.utils.navigate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryGameTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreen() {
    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ScreenBackground()
        MainContent()
    }
}

@Composable
private fun MainContent() {
    Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        GameLogo()
        Options()
    }
}

@Composable
private fun GameLogo() {
    MainLogo(modifier = Modifier
        .fillMaxWidth(0.9f)
        .fillMaxSize(0.3f)
    )
}

@Composable
private fun Options() {
    val context = LocalContext.current
    val activity = context as Activity
    fun goTo(destiny: Class<*>) = navigate(context, destiny)
    fun closeApp() = activity.finishAffinity()
    Column {
        Option("Iniciar", { goTo(ConfigTableActivity::class.java) })
        Option("Pontuações", { goTo(ScoresActivity::class.java) })
        Option("Regras", { goTo(RulesActivity::class.java) })
        Option("Sair", { closeApp() })
    }
}

@Composable
private fun Option(optionLabel: String, actionOnClick: () -> Unit = {}) {
    val modifiers = Modifier.padding(10.dp).clickable { actionOnClick() }
    Box(contentAlignment = Alignment.Center, modifier = modifiers) {
        OptionBase()
        OptionLabel(optionLabel)
    }
}

@Composable
private fun OptionLabel(optionLabel: String) {
    Text(
        text = optionLabel,
        color = Color.White,
        fontSize = 36.sp,
        fontWeight = FontWeight.Medium,
        style = TextStyle(shadow = Shadow(color = Color.Black, offset = Offset(6f, 6f), blurRadius = 12f))
    )
}

@Composable
private fun OptionBase() {
    Image(
        painter = painterResource(id = R.drawable.main_option),
        contentDescription = "Base com textura de madeira para exibir a opção",
        modifier = Modifier.fillMaxWidth(0.9f).height(95.dp)
    )
}