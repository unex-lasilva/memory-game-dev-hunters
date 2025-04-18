package br.mangarosa.memorygame.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.mangarosa.memorygame.classes.CardBoard
import br.mangarosa.memorygame.classes.Player
import br.mangarosa.memorygame.R
import br.mangarosa.memorygame.activities.components.CloseIcon
import br.mangarosa.memorygame.activities.components.FieldBase
import br.mangarosa.memorygame.activities.components.GameLogo
import br.mangarosa.memorygame.activities.components.ScreenBackground
import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme
import br.mangarosa.memorygame.activities.utils.navigate

private const val PLAYER_NAME_CHARS_LIMIT = 16
private val TABLE_SIZE_OPTIONS = listOf("4 x 4", "6 x 6")

class ConfigGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryGameTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    GameConfigScreen(intent)
                }
            }
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// IMPLEMENTAÇÃO DE COMPONENTES SIMPLES ///////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

/*
* Esta sessão contém a implementação de componentes simples e componentes-base, que podem ser usados
* para compor componentes mais complexos, que são formados a partir da junção de componentes menores.
*/

@Composable
private fun CloseIconAllignedAtEnd() {
    Column (horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
        CloseIcon()
    }
}

@Composable
private fun Title(titleText: String) {
    Text(
        text = titleText,
        color = Color.Black.copy(alpha = 0.7f),
        fontSize = 30.sp,
        fontWeight = FontWeight.SemiBold
    )
}

////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////// IMPLEMENTAÇÃO DOS COMPONENTES COMPOSTOS /////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

/*
* Esta sessão contém a implementação de componentes mais complexos, compostos por outros componentes.
* Para melhor organização, tais componentes foram representados por meio de objetos auto-invocáveis.
* Isto quer dizer que cada componente aqui possui seu conjunto de funções auxiliares (métodos
* privados que ajudam na renderização do componente), mas pode ser utilizado da mesma forma que se
* utiliza uma função.
*/

private object PlayButton {

    @Composable
    operator fun invoke(
        firstPlayerName: String,
        secondPlayerName: String,
        tableSizeSelected: String,
        playersNameStatus: String,
        intent: Intent
    ) {
        val isAbledToPlay = playersNameStatus == "OK"
        val context = LocalContext.current
        Box (contentAlignment = Alignment.Center, modifier = Modifier.clickable {
            if (isAbledToPlay) {
                runGame(
                    firstPlayerName,
                    secondPlayerName,
                    tableSizeSelected,
                    context
                )
            }
        }) {
            Base(isAbledToPlay)
            Label()
        }
    }

    @Composable
    private fun Base(isAbledToPlay: Boolean) {
        val modifiers = Modifier.width(200.dp).height(80.dp)
        if (isAbledToPlay) FieldBase.Green(modifier = modifiers) else FieldBase.Red(modifier = modifiers)
    }

    @Composable
    private fun Label() {
        Text(
            text = "JOGAR",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            style = TextStyle(shadow = Shadow(color = Color.Black, offset = Offset(6f, 6f), blurRadius = 12f))
        )
    }

    private fun runGame(
        firstPlayerName: String,
        secondPlayerName: String,
        tableSizeSelected: String,
        context: Context,
    ) {
        val newIntent = configIntent(firstPlayerName, secondPlayerName, tableSizeSelected, context)
        context.startActivity(newIntent)
    }

    private fun configIntent(
        firstPlayerName: String,
        secondPlayerName: String,
        tableSizeSelected: String,
        context: Context
    ): Intent {
        val players = configPlayers(firstPlayerName, secondPlayerName)
        val cardBoard = configCardBoard(tableSizeSelected)
        val intent = Intent(context, GameActivity::class.java)
        setPlayersOnIntent(intent, players)
        intent.putExtra("cardBoard", cardBoard)
        return intent
    }

    private fun setPlayersOnIntent(intent: Intent, players: List<Player>) {
        for (index in players.indices) {
            val playerNumber = index +1
            intent.putExtra("player${playerNumber}", players[index])
        }
    }

    private fun configPlayers(firstPlayerName: String, secondPlayerName: String): List<Player> {
        return listOf(
            Player("red", firstPlayerName),
            Player("blue", secondPlayerName)
        )
    }

    private fun configCardBoard(tableSizeSelected: String): CardBoard {
        val (lines, columns) = tableSizeSelected.split("x", ignoreCase = true).map { it.trim().toInt() }
        return CardBoard(lines, columns)
    }

}

private object PlayerNameField {
    private val RED_BORDER_COLOR = Color(0xFFDF3842)
    private val BLUE_BORDER_COLOR = Color(0xFF478CCA)
    private val BORDER_RADIUS = RoundedCornerShape(10.dp)
    private val BORDER_WEIGHT = 3.dp

    @Composable
    operator fun invoke(number: Int, name: String, color: String, onChange: (String) -> Unit) {
        TextField(
            value = name,
            onValueChange = { onChange(it) },
            placeholder = { Placeholder(number) },
            textStyle = TextStyle(textAlign = TextAlign.Center),
            label = { Label(number) },
            singleLine = true,
            colors = colorsPreset(color),
            modifier = Modifier.editableField(color)
        )

    }

    @Composable
    private fun Label(playerNumber: Int) {
        Text(
            text = "Participante $playerNumber",
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            color = Color.Black.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()

        )
    }

    @Composable
    private fun Placeholder(playerNumber: Int) {
        Text(
            text = "Participante $playerNumber",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Black.copy(alpha = 0.5f)
        )
    }

    @Composable
    private fun colorsPreset(color: String): TextFieldColors {
        val alphaLevel = 0.3f
        return TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.White.copy(alpha = alphaLevel)
        )
    }

    @Composable
    private fun Modifier.editableField(color: String): Modifier {
        val borderColor = if (color == "blue") BLUE_BORDER_COLOR else RED_BORDER_COLOR
        return this
            .height(70.dp)
            .clip(BORDER_RADIUS)
            .fillMaxWidth(fraction = 0.8f)
            .border(BORDER_WEIGHT, borderColor, BORDER_RADIUS)
    }

}

private object Status {

    @Composable
    operator fun invoke(firstPlayerName: String, secondPlayerName: String, updateFunction: (String) -> Unit) {
        val name1 = firstPlayerName.trim()
        val name2 = secondPlayerName.trim()
        val statusAndErrorWasFound = when {
            name1.isBlank() && name2.isBlank() -> Pair("", false)
            name1.isBlank() -> Pair(getPlayerNotIdentifiedMessage(1), true)
            name1.length > PLAYER_NAME_CHARS_LIMIT -> Pair(getPlayerNameTooBigMessage(1), true)
            name2.isBlank() -> Pair(getPlayerNotIdentifiedMessage(2), true)
            name2.length > PLAYER_NAME_CHARS_LIMIT -> Pair(getPlayerNameTooBigMessage(2), true)
            name1 == name2 -> Pair("Os participantes não podem ter a mesma identificação", true)
            else -> Pair("OK", false)
        }
        val status = statusAndErrorWasFound.first
        val errorWasFound = statusAndErrorWasFound.second
        updateFunction(status)
        if (errorWasFound) {
            ErrorMessage(message = status)
        }
    }

    @Composable
    private fun ErrorMessage(message: String) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = Color.Red,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 3.dp)
        )
    }

    private fun getPlayerNotIdentifiedMessage(playerNumber: Int): String {
        return "Participante $playerNumber não pode ficar com o nome em branco"
    }

    private fun getPlayerNameTooBigMessage(playerNumber: Int): String {
        val charsLimit = PLAYER_NAME_CHARS_LIMIT
        return "Participante $playerNumber tem um nome que excede $charsLimit caracteres. Reduza!"
    }

}

private object TableSizeOption {
    private const val CAPTION_TEXT = "Toque para alternar o tamanho"

    @Composable
    operator fun invoke(sizeOption: String, onClick: () -> Unit) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable { onClick() }
        ) {
            Base()
            Label(sizeOption)
        }
    }

    @Composable
    fun Caption() {
        Text(
            text = CAPTION_TEXT,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            textDecoration = TextDecoration.Underline
        )
    }

    @Composable
    private fun Label(textLabel: String) {
        Text(
            text = textLabel,
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            style = TextStyle(shadow = Shadow(color = Color.Black, offset = Offset(6f, 6f), blurRadius = 12f))
        )
    }

    @Composable
    private fun Base() {
        Image(
            painter = painterResource(id = R.drawable.table_option),
            contentDescription = "Base com textura de madeira para exibir a opção de tabuleiro",
            modifier = Modifier.height(78.dp).width(195.dp)
        )
    }

    fun getNext(tableSizeSelected: String): String {
        val index = TABLE_SIZE_OPTIONS.indexOf(tableSizeSelected)
        val nextIndex = if (index == TABLE_SIZE_OPTIONS.lastIndex) 0 else index +1
        return TABLE_SIZE_OPTIONS[nextIndex]

    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// IMPLEMENTAÇÃO DAS SESSÕES DE CONFIGURAÇÃO DO JOGO ////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun TableConfigSection(tableSizeSelected: String, onClick: () -> Unit) {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Title("TABULEIRO")
        Spacer(modifier = Modifier.padding(top = 10.dp))
        TableSizeOption(tableSizeSelected, onClick)
        Spacer(modifier = Modifier.padding(top = 5.dp))
        TableSizeOption.Caption()
    }
}

@Composable
private fun PlayerConfigSection(
    firstPlayerName: String,
    secondPlayerName: String,
    onFirstNameChange: (String) -> Unit,
    onSecondNameChange: (String) -> Unit,
    statusUpdateFunction: (String) -> Unit
) {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Title("PARTICIPANTES")
        Spacer(modifier = Modifier.padding(top = 10.dp))
        PlayerNameField(1, firstPlayerName, "red", onFirstNameChange)
        Spacer(modifier = Modifier.padding(top = 5.dp))
        PlayerNameField(2, secondPlayerName, "blue", onSecondNameChange)
        Status(firstPlayerName, secondPlayerName, statusUpdateFunction)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// IMPLEMENTAÇÕES VOLTADAS PARA O LAYOUT DO CONTEÚDO ////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

/*
* Esta sessão guarda as implementações que configuram o conteúdo e o seu layout na tela. Tais
* implementações configuram quais conteúdos devem aparecer ou não na tela bem como questões ligadas
* à estética, como por exemplo, espaçamento entre um conteúdo e outro.
*/

@Composable
private fun GameConfigContent(intent: Intent) {
    var firstPlayerName by remember { mutableStateOf("") }
    var secondPlayerName by remember { mutableStateOf("") }
    var tableSizeSelected by remember { mutableStateOf(TABLE_SIZE_OPTIONS[0]) }
    var playersNameStatus by remember { mutableStateOf("") }

    Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        PlayerConfigSection(
            firstPlayerName = firstPlayerName,
            secondPlayerName = secondPlayerName,
            onFirstNameChange = { firstPlayerName = it },
            onSecondNameChange = { secondPlayerName = it },
            statusUpdateFunction = { playersNameStatus = it }
        )
        Spacer(modifier = Modifier.padding(bottom = 15.dp))
        TableConfigSection(
            tableSizeSelected = tableSizeSelected,
            onClick = { tableSizeSelected = TableSizeOption.getNext(tableSizeSelected) }
        )
        Spacer(modifier = Modifier.padding(bottom = 15.dp))
        PlayButton(
            firstPlayerName = firstPlayerName,
            secondPlayerName = secondPlayerName,
            tableSizeSelected = tableSizeSelected,
            playersNameStatus = playersNameStatus,
            intent = intent
        )
    }
}

@Composable
private fun MainContent(intent: Intent) {
    Column (
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White.copy(alpha = 0.6f))
    ) {
        Spacer(modifier = Modifier.padding(top = 30.dp))
        CloseIconAllignedAtEnd()
        Spacer(modifier = Modifier.padding(bottom = 5.dp))
        GameConfigContent(intent)
        Spacer(modifier = Modifier.padding(bottom = 15.dp))
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////// IMPLEMENTAÇÃO DA DISPOSIÇÃO DOS ELEMENTOS NA TELA ////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

/*
* Esta sessão guarda as implementações que estruturam e organizam os elementos na tela.
*/

@Composable
private fun ContentBox(intent: Intent) {
    Box (modifier = Modifier.fillMaxWidth(fraction = 0.95f).fillMaxHeight(0.85f)) {
        Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            MainContent(intent)
        }
        Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            GameLogo()
        }
    }
}

@Composable
private fun GameConfigScreen(intent: Intent) {
    Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        ScreenBackground()
        ContentBox(intent)
    }
}