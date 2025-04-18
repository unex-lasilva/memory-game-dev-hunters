package br.mangarosa.memorygame.activities

import br.mangarosa.memorygame.classes.Player
import br.mangarosa.memorygame.classes.CardBoard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import br.mangarosa.memorygame.R
import kotlin.math.max

/**
 * @author Luan Vinicius
 *
 * Descrição: Implementação da GameActivity com lógica de cartas e pontuação.
 * Data: 13/04/2025 as 22:00
 */

/**
 * Classe que controla todo o estado do jogo, desde as cartas até a pontuação
 *
 * @param boardSize Tamanho do tabuleiro (4x4, 6x6, etc)
 * @param player1Name Nome do jogador 1
 * @param player2Name Nome do jogador 2
 * @param player1Color Cor das cartas do jogador 1 (padrão: Vermelho)
 * @param player2Color Cor das cartas do jogador 2 (padrão: Azul)
 */
class GameState(
    boardSize: Int,
    val player1Name: String,
    val player2Name: String,
    private val player1Color: Color,
    private val player2Color: Color
) {
    var currentPlayer by mutableIntStateOf(1)
    var player1Score by mutableIntStateOf(0)
    var player2Score by mutableIntStateOf(0)
    private val flippedIndices = mutableListOf<Int>()
    val matchedIndices = mutableSetOf<Int>()
    val cards = mutableStateListOf<Card>().apply {
        addAll(generateCards(boardSize))
    }
    var gameOver by mutableStateOf(false)
    var winner by mutableStateOf<String?>(null)

    private fun generateCards(size: Int): List<Card> {
        val totalPairs = (size * size) / 2
        val colors = mutableListOf<Color>().apply {
            add(Color.Black); add(Color.Black)
            repeat((totalPairs - 1) / 2) {
                add(player1Color); add(player1Color)
                add(player2Color); add(player2Color)
            }
            repeat(totalPairs - 1 - ((totalPairs - 1) / 2 * 2)) {
                add(Color.Yellow); add(Color.Yellow)
            }
        }
        return colors.shuffled().mapIndexed { index, color -> Card(index, color) }
    }

    fun flipCard(index: Int): Boolean {
        if (gameOver || index !in cards.indices || cards[index].isMatched) return false
        if (flippedIndices.size >= 2 || index in flippedIndices) return false

        cards[index] = cards[index].copy(isFlipped = true)
        flippedIndices.add(index)

        if (flippedIndices.size == 2) {
            checkMatch()
            checkGameEnd()
        }
        return true
    }

    private fun checkMatch() {
        val (first, second) = flippedIndices
        val card1 = cards[first]
        val card2 = cards[second]
        val isMatch = card1.color == card2.color

        if (isMatch) {
            cards[first] = card1.copy(isMatched = true)
            cards[second] = card2.copy(isMatched = true)
            matchedIndices.addAll(listOf(first, second))
            updateScore(card1.color, true)
        } else {
            updateScore(card1.color, false)
            Handler(Looper.getMainLooper()).postDelayed({
                cards[first] = card1.copy(isFlipped = false)
                cards[second] = card2.copy(isFlipped = false)
                flippedIndices.clear()
                currentPlayer = if (currentPlayer == 1) 2 else 1
            }, 1000)
            return
        }

        flippedIndices.clear()
        cards.forEachIndexed { index, card ->
            if (!card.isMatched) {
                cards[index] = card.copy(isFlipped = false)
            }
        }
    }

    private fun checkGameEnd() {
        if (cards.all { it.isMatched }) {
            gameOver = true
            winner = when {
                player1Score > player2Score -> player1Name
                player2Score > player1Score -> player2Name
                else -> "Empate"
            }
        }
    }

    private fun updateScore(color: Color, isMatch: Boolean) {
        val flippedColors = flippedIndices.map { cards[it].color }
        val currentScore = if (currentPlayer == 1) ::player1Score else ::player2Score

        when {
            color == Color.Black && isMatch -> currentScore.set(currentScore.get() + 50)
            flippedColors.contains(Color.Black) && flippedColors.any { it != Color.Black } ->
                currentScore.set(max(0, currentScore.get() - 50))
            flippedColors.contains(Color.Yellow) && flippedColors.any { it != Color.Yellow } ->
                currentScore.set(max(0, currentScore.get() - 1))
            flippedColors.contains(player1Color) && flippedColors.contains(player2Color) ->
                currentScore.set(max(0, currentScore.get() - 2))
            color == Color.Yellow && isMatch -> currentScore.set(currentScore.get() + 1)
            color == player1Color && currentPlayer == 1 && isMatch -> currentScore.set(currentScore.get() + 5)
            color == player2Color && currentPlayer == 2 && isMatch -> currentScore.set(currentScore.get() + 5)
            color == player2Color && currentPlayer == 1 && isMatch -> currentScore.set(currentScore.get() + 1)
            color == player1Color && currentPlayer == 2 && isMatch -> currentScore.set(currentScore.get() + 1)
        }
    }
}

/**
 * Representa uma carta do jogo
 *
 * @param id Identificador único da carta
 * @param color Cor da carta
 * @param isFlipped Se está virada
 * @param isMatched Se já foi combinada com seu par
 */
data class Card(
    val id: Int,
    val color: Color,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

/**
 * Activity principal que inicia o jogo
 *
 * Responsável por:
 * - Criar a tela do jogo
 * - Aplicar o tema visual
 * - Gerenciar a navegação básica (voltar/sair)
 */
class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val player1 = intent.getSerializableExtra("player1") as? Player
        val player2 = intent.getSerializableExtra("player2") as? Player
        val cardBoard = intent.getSerializableExtra("cardBoard") as? CardBoard

        // Função para converter string para Color
        fun String.toColor(): Color {
            return when (this.toLowerCase()) {
                "red" -> Color.Red
                "blue" -> Color.Blue
                "yellow" -> Color.Yellow
                "black" -> Color.Black
                else -> Color.Gray
            }
        }

        setContent {
            MemoryGameTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GameScreen(
                        player1Name = player1?.nickname ?: "Jogador 1",
                        player2Name = player2?.nickname ?: "Jogador 2",
                        boardSize = cardBoard?.lines ?: 4,
                        player1Color = player1?.color?.toColor() ?: Color.Red,
                        player2Color = player2?.color?.toColor() ?: Color.Blue,
                        onBackClick = { finish() },
                        onGameFinished = { finish() }
                    )
                }
            }
        }
    }
    /**
     * Tema visual personalizado do jogo
     *
     * Atualmente apenas aplica o MaterialTheme padrão.
     *
     * @param content Conteúdo que terá o tema aplicado
     */
    @Composable
    fun MemoryGameTheme(content: @Composable () -> Unit) {
        MaterialTheme(content = content)
    }

    /**
     * Tela principal do jogo que monta toda a interface
     *
     * @param player1Name Nome do jogador 1 (padrão: "Jogador 1")
     * @param player2Name Nome do jogador 2 (padrão: "Jogador 2")
     * @param boardSize Tamanho do tabuleiro (padrão: 4)
     * @param onBackClick Ação quando clica no botão voltar
     * @param onGameFinished Ação quando o jogo termina
     */
    @Preview
    @Composable
    fun GameScreen(
        player1Name: String = "Jogador 1",
        player2Name: String = "Jogador 2",
        boardSize: Int = 4,
        player1Color: Color = Color.Red,
        player2Color: Color = Color.Blue,
        onBackClick: () -> Unit = {},
        onGameFinished: () -> Unit = {}
    ) {
        var gameState by remember {
            mutableStateOf(
                GameState(
                    boardSize = boardSize,
                    player1Name = player1Name,
                    player2Name = player2Name,
                    player1Color = player1Color,
                    player2Color = player2Color
                )
            )
        }

        var showGameOverDialog by remember { mutableStateOf(false) }

        LaunchedEffect(gameState.gameOver) {
            if (gameState.gameOver) showGameOverDialog = true
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.wallpaper),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo marca d'água",
                    modifier = Modifier
                        .size(400.dp)
                        .offset(y = 90.dp)
                        .alpha(0.6f),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameHeader(
                    player1Name = gameState.player1Name,
                    player2Name = gameState.player2Name,
                    player1Score = gameState.player1Score,
                    player2Score = gameState.player2Score,
                    currentPlayer = gameState.currentPlayer
                )

                Box(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .fillMaxWidth()
                        .offset(y = (-20).dp)
                        .widthIn(max = 600.dp)
                        .padding(horizontal = 12.dp, vertical = 67.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                            .padding(1.dp)
                    ) {
                        ResponsiveGameBoard(
                            gameState = gameState,
                            showMatch = false,
                            rows = boardSize,
                            cols = boardSize
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF60B7D1))
                ) {
                    Text("Voltar", fontSize = 14.sp)
                }
            }
        }

        if (showGameOverDialog) {
            GameOverDialog(
                winner = gameState.winner,
                onPlayAgain = {
                    showGameOverDialog = false
                    gameState = GameState(boardSize, player1Name, player2Name, player1Color, player2Color)
                },
                onExit = {
                    showGameOverDialog = false
                    onGameFinished()
                }
            )
        }
    }

    @Composable
    private fun GameOverDialog(
        winner: String?,
        onPlayAgain: () -> Unit,
        onExit: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onExit,
            title = {
                Text(
                    text = "Jogo Finalizado!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "O vencedor é: ${winner ?: "Empate"}",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = onPlayAgain,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF60B7D1))
                ) {
                    Text("Jogar Novamente")
                }
            },
            dismissButton = {
                Button(
                    onClick = onExit,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Sair")
                }
            },
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        )
    }


    /**
     * Componente que renderiza o tabuleiro do jogo de forma responsiva
     *
     * Calcula automaticamente o tamanho das cartas para se adaptar a diferentes telas
     * mantendo a proporção correta entre largura e altura
     *
     * @param gameState Estado atual do jogo com as cartas e informações
     * @param showMatch Se deve mostrar as cartas viradas
     * @param rows Quantidade de linhas do tabuleiro
     * @param cols Quantidade de colunas do tabuleiro
     */
    @Composable
    fun ResponsiveGameBoard(
        gameState: GameState,
        showMatch: Boolean,
        rows: Int,
        cols: Int
    ) {
        // Usa BoxWithConstraints para pegar as dimensões disponíveis
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // Espaçamento padrão entre as cartas
            val cardSpacing = 1.dp

            // Calcula o espaçamento total necessário
            val totalHorizontalSpacing = cardSpacing * (cols - 1)
            val totalVerticalSpacing = cardSpacing * (rows - 1)

            // Calcula o tamanho máximo que cada carta pode ter
            // considerando o espaço disponível e o espaçamento
            val maxCardWidth = (maxWidth - totalHorizontalSpacing) / cols
            val maxCardHeight = (maxHeight - totalVerticalSpacing) / rows

            // Define o tamanho final mantendo a proporção 1.55:1 (altura:largura)
            val cardWidth = min(maxCardWidth, maxCardHeight / 1.55f)
            val cardHeight = cardWidth * 1.55f

            // Layout em coluna que centraliza verticalmente o tabuleiro
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                // Loop para criar as linhas do tabuleiro
                repeat(rows) { row ->

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Loop para criar as colunas/cartas de cada linha
                        repeat(cols) { col ->
                            val index = row * cols + col
                            Box(
                                modifier = Modifier
                                    .width(cardWidth)
                                    .height(cardHeight)
                            ) {
                                // Componente de carta individual
                                CardView(
                                    color = gameState.cards[index].color,
                                    isFlipped = gameState.cards[index].isFlipped,
                                    isMatched = gameState.matchedIndices.contains(index),
                                    onClick = { if (!showMatch) gameState.flipCard(index) },
                                    cardHeight = cardHeight
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Componente que representa uma carta individual do jogo
     *
     * Controla a animação de virar e os efeitos visuais quando combinada
     *
     * @param color Cor da face frontal da carta
     * @param isFlipped Se a carta está atualmente virada
     * @param isMatched Se a carta foi combinada com seu par
     * @param onClick Callback quando a carta é clicada
     * @param cardHeight Altura da carta (largura é calculada proporcionalmente)
     */
    @Composable
    fun CardView(
        color: Color,
        isFlipped: Boolean,
        isMatched: Boolean,
        onClick: () -> Unit,
        cardHeight: Dp
    ) {
        // Calcula a largura baseada na altura mantendo proporção 1:1.55
        val cardWidth = cardHeight / 1.55f

        // Configurações visuais fixas
        val cornerRadius = 8.dp
        val animationDuration = 500 // tempo da animação em milissegundos
        val density = LocalDensity.current.density // densidade da tela

        // Animação de rotação (virar a carta)
        val rotation by animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(durationMillis = animationDuration)
        )

        // Animação de escala (quando encontra par)
        val scale by animateFloatAsState(
            targetValue = if (isMatched) 0.95f else 1f,
            animationSpec = tween(durationMillis = animationDuration)
        )

        // Gradiente sutil para o verso da carta
        val textureGradient = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.2f),
                Color.DarkGray.copy(alpha = 0.1f)
            ),
            start = Offset(0f, 0f),
            end = Offset(100f, 100f)
        )

        // Cor do brilho quando combinada
        val glowColor by animateColorAsState(
            targetValue = if (isMatched) Color.Green.copy(alpha = 0.3f) else Color.Transparent,
            animationSpec = tween(durationMillis = animationDuration)
        )

        // Sombra mais intensa quando virada
        val elevation = if (isFlipped) 8.dp else 4.dp

        // Container principal da carta
        Box(
            modifier = Modifier
                .width(cardWidth)
                .height(cardHeight)
                .padding(8.dp)
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(cornerRadius),
                    clip = true,
                )
                .graphicsLayer {
                    rotationY = rotation // Aplica rotação 3D
                    cameraDistance = 8 * density // Configura perspectiva
                    scaleX = scale // Aplica escala
                    scaleY = scale
                    shape = RoundedCornerShape(cornerRadius)
                    clip = true
                }
                .clickable(
                    onClick = onClick,
                    enabled = !isMatched // Só clicável se não foi combinada
                ),
            contentAlignment = Alignment.Center
        ) {
            // Verso da carta (mostra quando não está totalmente virada)
            if (rotation < 90f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White, RoundedCornerShape(cornerRadius))
                ) {
                    // Textura de fundo
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(textureGradient)
                    )

                    // Imagem central do verso
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.versenuvem),
                            contentDescription = "Verso da carta",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(cornerRadius - 2.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color.Black.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(cornerRadius - 2.dp)
                                )
                        )
                    }

                    // Borda decorativa
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 2.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.8f),
                                        Color.Gray.copy(alpha = 0.5f)
                                    )
                                ),
                                shape = RoundedCornerShape(cornerRadius)
                            )
                    )
                }
            } else {
                // Frente da carta (mostra quando está virada)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color, RoundedCornerShape(cornerRadius))
                        .border(
                            width = 1.5.dp,
                            color = Color.Black.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(cornerRadius))
                ) {
                    // Efeito especial quando combinada
                    if (isMatched) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(glowColor)
                                .border(
                                    width = 3.dp,
                                    color = Color.Green.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(cornerRadius))
                        )
                    }
                }
            }
        }
    }

    /**
     * Cabeçalho do jogo que mostra os placares e jogadores
     *
     * Exibe:
     * - Nome e pontuação de cada jogador
     * - Destaque visual para o jogador da vez
     * - Design consistente com o tema do jogo
     *
     * @param player1Name Nome do jogador 1
     * @param player2Name Nome do jogador 2
     * @param player1Score Pontos do jogador 1
     * @param player2Score Pontos do jogador 2
     * @param currentPlayer Número do jogador atual (1 ou 2)
     */
    @Composable
    fun GameHeader(
        player1Name: String,
        player2Name: String,
        player1Score: Int,
        player2Score: Int,
        currentPlayer: Int,
        modifier: Modifier = Modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .offset(y = 15.dp), // Reduzi o padding
            color = Color(0xFF60B7D1),
            shape = RoundedCornerShape(7.dp),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp), // Reduzi o padding
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerInfo(
                    name = player1Name,
                    score = player1Score,
                    backgroundColor = Color(0xFFb83c2e),
                    isCurrentPlayer = currentPlayer == 1
                )

                Text(
                    text = "X",
                    fontSize = 50.sp, // Reduzi um pouco o tamanho
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                PlayerInfo(
                    name = player2Name,
                    score = player2Score,
                    backgroundColor = Color(0xFF0d56a3),
                    isCurrentPlayer = currentPlayer == 2
                )
            }
        }
    }

    /**
     * Componente que exibe as informações de um jogador
     *
     * @param name Nome do jogador
     * @param score Pontuação atual
     * @param backgroundColor Cor de fundo do placar
     * @param isCurrentPlayer Se é o jogador da vez
     */
    @Composable
    private fun PlayerInfo(
        name: String,
        score: Int,
        backgroundColor: Color,
        isCurrentPlayer: Boolean
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Container do placar
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .border(
                        width = if (isCurrentPlayer) 4.8.dp else 3.dp,
                        color = if (isCurrentPlayer) Color.White else backgroundColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                // Texto da pontuação
                Text(
                    text = score.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp
                )
            }

            // Texto do nome
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = name,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
        }
    }
}