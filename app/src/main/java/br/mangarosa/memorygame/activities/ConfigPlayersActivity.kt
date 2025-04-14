package br.mangarosa.memorygame.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.mangarosa.memorygame.R
import br.mangarosa.memorygame.activities.components.GreenNext
import br.mangarosa.memorygame.activities.components.Home
import br.mangarosa.memorygame.activities.components.MainText
import br.mangarosa.memorygame.activities.components.RedBack
import br.mangarosa.memorygame.activities.components.ScreenBackground
import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme
import br.mangarosa.memorygame.activities.utils.navigate
import br.mangarosa.memorygame.classes.CardBoard
import br.mangarosa.memorygame.classes.Player

const val PLAYER_NAME_CHARS_LIMIT = 16

val ASSETS_PER_COLOR = mapOf(
    "red" to R.drawable.red_square,
    "blue" to R.drawable.blue_square
)

class ConfigPlayersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryGameTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    ConfigScreen(intent)
                }
            }
        }
    }
}

@Composable
private fun ConfigScreen(intent: Intent) {
    Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        ScreenBackground()
        MainContent(intent)
    }
}

@Composable
private fun MainContent(intent: Intent) {
    var playerName1 by remember { mutableStateOf("") }
    var playerName2 by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Title()
        PlayerNameField(1, playerName1, "red", onNameChange = { playerName1 = it })
        PlayerNameField(2, playerName2, "blue", onNameChange = { playerName2 = it })
        Status(playerName1, playerName2, callUpdateFunction = { status = it })
        NavigationItems(playerName1, playerName2, status, intent)
    }
}

@Composable
private fun Status(playerName1: String, playerName2: String, callUpdateFunction: (String) -> Unit) {
    fun getPlayerNotIdentifiedMessage(playerNumber: Int) =
        "Participante $playerNumber não pode ficar com o nome em branco"
    fun getPlayerNameTooBigMessage(playerNumber: Int) =
        "Participante $playerNumber tem um nome que excede $PLAYER_NAME_CHARS_LIMIT caracteres. Reduza!"
    val name1 = playerName1.trim()
    val name2 = playerName2.trim()
    val statusAndMustBeShown = when {
        name1.isBlank() && name2.isBlank() -> Pair("", false)
        name1.isBlank() -> Pair(getPlayerNotIdentifiedMessage(1), true)
        name2.isBlank() -> Pair(getPlayerNotIdentifiedMessage(2), true)
        name1.length > PLAYER_NAME_CHARS_LIMIT -> Pair(getPlayerNameTooBigMessage(1), true)
        name2.length > PLAYER_NAME_CHARS_LIMIT -> Pair(getPlayerNameTooBigMessage(2), true)
        name1 == name2 -> Pair("Os participantes não podem ter a mesma identificação", true)
        else -> Pair("OK", false)
    }
    val status = statusAndMustBeShown.first
    val showStatus = statusAndMustBeShown.second
    callUpdateFunction(status)
    if (showStatus) {
        TextStatus(textStatus = status, color = Color.Red)
    }
}

@Composable
private fun TextStatus(textStatus: String, color: Color, fontSize: TextUnit = 15.sp, modifier: Modifier = Modifier) {
    Text(
        text = textStatus,
        textAlign = TextAlign.Center,
        color = color,
        fontSize = fontSize,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.padding(bottom = 15.dp)
    )
}

@Composable
private fun PlayerNameField(playerNumber: Int, playerName: String, playerColor: String, onNameChange: (String) -> Unit) {
    val indicatorColor = Color(0xFF478CCA)
    val containerColor = Color(0xFFAFD7F6)
    val labelColor = Color.DarkGray
    val textColor = Color.Black
    val defaultAlpha = 0.4f
    TextField(
        value = playerName,
        onValueChange = { onNameChange(it) },
        label = { Text("Participante $playerNumber") },
        leadingIcon = { ColorIndicator(playerColor) },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = containerColor.copy(alpha = defaultAlpha),
            unfocusedIndicatorColor = indicatorColor,
            unfocusedPlaceholderColor = Color(0xFF777777),
            unfocusedLabelColor = labelColor,
            unfocusedTextColor = textColor,
            focusedContainerColor = containerColor.copy(alpha = defaultAlpha * 1.5f),
            focusedIndicatorColor = indicatorColor,
            focusedPlaceholderColor = Color(0xFF2A2A2A),
            focusedLabelColor = labelColor,
            focusedTextColor = textColor,
        ),
        modifier = Modifier
            .padding(bottom = 15.dp)
            .height(70.dp)
            .fillMaxWidth(fraction = 0.9f)
    )
}

@Composable
private fun ColorIndicator(color: String) {
    Image(
        painter = painterResource(id = ASSETS_PER_COLOR.getValue(color)),
        contentDescription = "Um quadrado $color com borda branca para indicar quem jogará com esta cor",
        modifier = Modifier
            .padding(start = 10.dp, end = 5.dp)
            .size(55.dp)
    )
}

@Composable
private fun Title() {
    MainText(
        text = "IDENTIFIQUE OS JOGADORES",
        fontSize = 40,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun NavigationItems(playerName1: String, playerName2: String, status: String, intent: Intent){
    Row {
        BackIcon()
        HomeIcon()
        if (status == "OK") {
            NextIcon(playerName1, playerName2, intent)
        }
    }
}

@Composable
fun Modifier.iconified(): Modifier {
    return this.size(75.dp).padding(end = 10.dp)
}

@Composable
fun Modifier.navigable(nextActivity: Class<*>): Modifier {
    val context = LocalContext.current
    return this.clickable { navigate(context, nextActivity) }
}

@Composable
fun BackIcon() {
    RedBack(modifier = Modifier.iconified().navigable(ConfigTableActivity::class.java))
}

@Composable
fun HomeIcon() {
    Home(modifier = Modifier.iconified().navigable(MainActivity::class.java))
}

@Composable
fun NextIcon(playerName1: String, playerName2: String, intent: Intent) {
    val context = LocalContext.current
    val tableSize = intent.getStringExtra("tableSize")?.split("x")?.map { it.trim().toInt() }
    val lines = tableSize?.get(0) ?: 4
    val columns = tableSize?.get(1) ?: 4
    GreenNext(modifier = Modifier.iconified().clickable {
        val newIntent = Intent(context, GameActivity::class.java)
            .putExtra("player1", Player("red", playerName1))
            .putExtra("player2", Player("blue", playerName2))
            .putExtra("cardBoard", CardBoard(lines, columns))
        context.startActivity(newIntent)
    })
}