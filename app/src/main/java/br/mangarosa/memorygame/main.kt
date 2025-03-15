package br.mangarosa.memorygame

import kotlin.random.Random
import kotlin.reflect.KClass

const val GAME_NAME = "MANGA ROSA MEMORY GAME"
const val NUMBER_OF_COPIES_PER_CARD = 2
const val NUMBER_OF_BLACK_CARDS = 1

const val RED_CARDS_AMOUNT_PCT = 0.25f
const val BLUE_CARDS_AMOUNT_PCT = 0.25f
const val RETRIES_LIMIT_NUMBER = 3
const val FIX_POSITION_0_TO_1 = true

const val BLACK_CARD_POINTS = 50
const val OWN_CARD_POINTS = 5
const val DEFAULT_LOST_POINTS = 2

const val COLUMN_LABEL = "COLUNA: "
const val LINE_LABEL = "LINHA: "
const val PLAYER_LABEL = "PARTICIPANTE: "

const val FACEUP_ERROR_MESSAGE = "A carta da posição informada já está virada."
const val LINE_FULLY_FACEUP_ERROR_MESSAGE = "Todas as cartas desta linha já estão viradas."
const val POSITION_ERROR_MESSAGE = "Posição da carta inválida."

const val FACEUP_RETRY_MESSAGE = "Por favor, escolha outra posição!"
const val POSITION_RETRY_MESSAGE = "Por favor, insira uma posição válida!"

const val DEFAULT_TERMINAL_WIDTH_SIZE = 80
const val DEFAULT_PADDING = " "
const val DEFAULT_LABEL_PADDING_NUMBER = 2
const val DEFAULT_WAIT_SEGS_POS_ACTION = 3

val COLORS_AVAILABLE_TO_PLAY = listOf<String>("red", "blue")
val SPECIAL_COLORS = listOf<String>("black", "yellow")

val ASCII_COLORS = mapOf (
    "black" to "\u001B[30m",
    "red" to "\u001B[31m",
    "green" to "\u001B[32m",
    "yellow" to "\u001B[33m",
    "blue" to "\u001B[34m",
    "white" to "\u001B[37m",
    "reset" to "\u001B[0m",
)

val ASCII_BG_COLORS = mapOf (
    "red" to "\u001B[41m",
    "blue" to "\u001B[44m",
    "black" to "\u001B[40m",
    "white" to "\u001B[47m",
    "yellow" to "\u001B[43m",
    "reset" to "\u001B[0m"
)

val ASCII_STYLES = mapOf (
    "bold" to "\u001B[1m",
    "underline" to "\u001B[4m",
    "reset" to "\u001B[0m"
)

typealias CardsCollection = MutableList<Card>
typealias ColorString = String
typealias LineOfCards = List<Card>
typealias PlayersMap = Map<ColorString, Player>

fun cardLabel(cardNumber: Int) = "DIGITE A POSIÇÃO DA ${cardNumber}ª CARTA QUE DESEJA REVELAR: "
fun pointsLabel(points: Int) = points.toString() + if (points in -1..1) " PONTO" else " PONTOS"
fun timesLabel() = RETRIES_LIMIT_NUMBER.toString() + if (RETRIES_LIMIT_NUMBER in -1..1) " VEZ" else " VEZES"

/* *************************************************************************************************
*                                   SESSÃO DE FUNÇÕES DE EXTENSÃO
*                   Novas capacidades foram dadas aos valores de tipo String e Int
***************************************************************************************************/

fun Int.isPair() = this % 2 == 0
fun Int.half() = this / 2

fun String.bold() = "${ASCII_STYLES["bold"]}$this${ASCII_STYLES["reset"]}"
fun String.setColor(color: String) = "${ASCII_COLORS[color]}$this${ASCII_COLORS["reset"]}"
fun String.setBgColor(color: String) = "${ASCII_BG_COLORS[color]}$this${ASCII_BG_COLORS["reset"]}"

fun String.center(widthSize: Int = DEFAULT_TERMINAL_WIDTH_SIZE, paddingStr: String = " "): String {
    val paddingNumber = (widthSize - this.length)
    val paddingNumberForEachSide = (paddingNumber / 2).toInt()
    val halfPadding = paddingStr.repeat(paddingNumberForEachSide)
    var newString = halfPadding + this + halfPadding
    newString += paddingStr.repeat(widthSize - newString.length)
    return newString
}

/* *************************************************************************************************
*                                   SESSÃO DE OBJETOS
*  Os objetos aqui foram criados com o intuito de agrupar funções e recursos de categoria similar
***************************************************************************************************/

object ComponentBuilder {
    const val DEFAULT_SQUARE_WIDTH = 4

    class Square(val color: String, val length: Int = DEFAULT_SQUARE_WIDTH, val backgroundStr: String = DEFAULT_PADDING) {
        override fun toString() = backgroundStr.repeat(length).setBgColor(color)
    }

    class PlayerInfo(val player: Player, val paddingStr: String = DEFAULT_PADDING) {
        val padding = paddingStr.repeat(DEFAULT_LABEL_PADDING_NUMBER)
        val length = DEFAULT_SQUARE_WIDTH + DEFAULT_LABEL_PADDING_NUMBER + player.label().length
        val colorIndicator = Square(player.color)
        override fun toString() = "${colorIndicator}$padding${player.label().bold()}"
    }

    class PlayersInfo(vararg players: Player, widthSize: Int = DEFAULT_TERMINAL_WIDTH_SIZE) {
        val playersInfo = players.map { PlayerInfo(it) }
        val paddingNumberBetweenPlayers = (widthSize - this.length()) / (playersInfo.size - 1)

        override fun toString(): String {
            var string = ""
            val padding = if (paddingNumberBetweenPlayers >= 0) {
                " ".repeat(paddingNumberBetweenPlayers)
            } else {
                "\n".repeat(2)
            }
            playersInfo.forEach { string += it.toString() + padding }
            return string.trim()
        }

        fun length(): Int {
            var length = 0
            playersInfo.forEach { length += it.length }
            return length
        }
    }

    class Separator(val sepStr: String = "-", val widthSize: Int = DEFAULT_TERMINAL_WIDTH_SIZE) {
        override fun toString() = sepStr.repeat(widthSize)
    }

    class Title(
        val name: String = GAME_NAME,
        val topSep: String = "=",
        val downSep: String = "-",
        val width: Int = DEFAULT_TERMINAL_WIDTH_SIZE
    ) {
        override fun toString(): String {
            val sep1 = Separator(topSep, width).toString()
            val sep2 = Separator(downSep, width).toString()
            val titleName = name.center(width)
            return sep1 + "\n" + titleName + "\n" + sep2
        }
    }

    class ScoreBoard(
        vararg players: Player,
        val width: Int = DEFAULT_TERMINAL_WIDTH_SIZE,
        val downSep: String="-"
    ) {
        val playersInfo = PlayersInfo(*players)
        val title = Title(downSep = "", width = width)
        override fun toString() = "$title\n${playersInfo}\n${Separator(this.downSep, width)}"
    }

}

object ActionStatus {
    val nothingHappensMessage = "ERROU! NADA ACONTECE... "

    fun nextPlayerMessage(nextPlayer: Player) = "PASSOU A VEZ PARA \"${nextPlayer.nickname}\""
    fun successMessage(points: String) = "ACERTOU! GANHOU ${points}. CONTINUE JOGANDO."
    fun wrongMessage(points: Int) = "ERROU! PERDEU ${pointsLabel(points)}. "

    fun showStatus(text: String, segsToWait: Int = DEFAULT_WAIT_SEGS_POS_ACTION) {
        println(text.bold())
        for (seg in 1..segsToWait) {
            val segsLefting = segsToWait - seg
            println("PROSSEGUINDO PARA A PRÓXIMA AÇÃO EM $segsLefting...")
            Thread.sleep(1000)
        }
    }

    fun printNothingHappens(nextPlayer: Player) {
        val message = nothingHappensMessage + "\n" + nextPlayerMessage(nextPlayer)
        showStatus(message)
    }

    fun printRetryExceeded(nextPlayer: Player) {
        val topMessage = "POR TER TENTADO ${timesLabel()} SEM SUCESSO..."
        val finalMessage = "PERDEU A VEZ PARA ${nextPlayer.nickname}!"
        val message = topMessage + "\n" + finalMessage
        showStatus(message.setColor("red"))
    }

    fun printSuccess(pointsEarned: Int) {
        showStatus(successMessage(pointsLabel(pointsEarned)).setColor("green"), 2)
    }

    fun printWinner(players: List<Player>) {
        val scores = players.map { it.score }
        val higgestScore = scores.max()
        val playersWithHiggestScore = players.filter { it.score == higgestScore }
        if (playersWithHiggestScore.size > 1) {
            var playersInTie = "EMPATE ENTRE: "
            for (playerIndex in playersWithHiggestScore.indices) {
                val player = playersWithHiggestScore[playerIndex]
                playersInTie += when (playerIndex) {
                    0 -> player.nickname
                    playersWithHiggestScore.lastIndex -> " e ${player.nickname}."
                    else -> ", ${player.nickname}"
                }
            }
            showStatus(playersInTie.setColor("yellow"))
        } else {
            val winnerName = playersWithHiggestScore.first().nickname
            showStatus("PARTICIPANTE \"$winnerName\" VENCEU O JOGO.".setColor("green"), 0)
        }
    }

    fun printWrong(lostPoints: Int, nextPlayer: Player) {
        val message = wrongMessage(lostPoints) + "\n" + nextPlayerMessage(nextPlayer)
        showStatus(message.setColor("red"))
    }
}

/* *************************************************************************************************
*                                   SESSÃO DE CLASSES
*Elementos dentro do jogo representados por meio de entidades, com seus comportamentos implementados
***************************************************************************************************/

class Card(val code: String, val color: String, var isFaceUp: Boolean = false)

class Player(val color: String, val nickname: String, score: Int = 0) {
    var score: Int = getValidScoreOrZero(score)
        set (value) { field = getValidScoreOrZero(value) }

    private fun getValidScoreOrZero(newScore: Int): Int {
        return if (newScore >= 0) newScore else 0
    }

    fun label() = "${this.nickname} - ${this.scoreLabel()}"
    fun scoreLabel() = this.score.toString() + if (this.score in -1..1) " ponto" else " pontos"

    fun upScore(pointsEarned: Int) {
        this.score += pointsEarned
    }

    fun downScore(lostPoints: Int) {
        this.score = getValidScoreOrZero(this.score - lostPoints)
    }
}

class CardBoard(val lines: Int, val columns: Int) {
    val cards = mutableListOf<LineOfCards>()
    lateinit var players: PlayersMap

    // Métodos para a configuração do tabuleiro

    init {
        val numberOfUniqueCardsPerColor = getNumberOfUniqueCardsPerColor()
        val cardsForTheBoard = generateCards(numberOfUniqueCardsPerColor)
        repeat (lines) {
            val cardsOnLine = mutableListOf<Card>()
            repeat (columns) {
                cardsOnLine.add(cardsForTheBoard.first())
                cardsForTheBoard.removeAt(0)
            }
            this.cards.add(cardsOnLine)
        }
    }

    private fun getNumberOfUniqueCardsPerColor(): Map<ColorString, Int> {
        val numberOfUniqueCards = ((this.lines * this.columns) / NUMBER_OF_COPIES_PER_CARD).toInt()
        val numberOfCardsPerColor = mutableMapOf(
            "red" to (numberOfUniqueCards * RED_CARDS_AMOUNT_PCT).toInt(),
            "blue" to (numberOfUniqueCards * BLUE_CARDS_AMOUNT_PCT).toInt(),
            "black" to NUMBER_OF_BLACK_CARDS
        )
        val numberOfCardsWithColorNow = numberOfCardsPerColor.values
            .reduce { totalNumber, cardsNumberForColor -> cardsNumberForColor + totalNumber }
        val numberOfCardsWithNoColorNow = numberOfUniqueCards - numberOfCardsWithColorNow
        val numberOfYellowCards = if (numberOfCardsWithNoColorNow < 0) 0 else numberOfCardsWithNoColorNow
        numberOfCardsPerColor["yellow"] = numberOfYellowCards
        return numberOfCardsPerColor
    }

    private fun generateCards(numberOfUniqueCardsPerColor: Map<ColorString, Int>): CardsCollection {
        val cards = mutableListOf<Card>()
        numberOfUniqueCardsPerColor.forEach {
            val color = it.key
            val numberOfUniqueCards = it.value
            val codes = getUniqueCodesForCards(numberOfUniqueCards)
            codes.forEach {
                val code = it
                repeat (NUMBER_OF_COPIES_PER_CARD) {
                    val card = Card(code, color)
                    cards.add(card) }
            }
        }
        repeat (NUMBER_OF_COPIES_PER_CARD) { cards.shuffle() }
        return cards
    }

    private fun getUniqueCodesForCards(numberOfUniqueCards: Int): Set<String> {
        val codes = mutableSetOf<String>()
        while (codes.size < numberOfUniqueCards) {
            val code = ('A'..'Z').random() + Random.nextInt(0, 9).toString()
            codes.add(code)
        }
        return codes
    }

    // Métodos para obter informações do tabuleiro envolvendo cartas

    fun hasFaceDownCards(): Boolean {
        this.cards.forEach { it.forEach { if (!it.isFaceUp) return true } }
        return false
    }

    fun hasFaceDownCards(lineIndex: Int): Boolean {
        this.cards[lineIndex].forEach { if (!it.isFaceUp) return true }
        return false
    }

    fun columnHasFaceDownCards(columnIndex: Int): Boolean {
        for (lineIndex in this.cards.indices) {
            if (!this.cards[lineIndex][columnIndex].isFaceUp) {
                return true
            }
        }
        return false
    }

    // Métodos para obter uma carta dentro do tabuleiro, junto com seus auxiliares (privados)

    fun getCards(vararg cardsLabels: String): List<Card>? {
        val cards = mutableListOf<Card>()
        cardsLabels.forEach {
            val card = getCard(it)
            if (card == null) {
                cards.forEach { it.isFaceUp = false }
                return null
            }
            card.isFaceUp = true
            println()
            this.render()
            cards.add(card)
            println()
        }
        return cards
    }

    fun getCard(infoLabel: String): Card? {
        println(infoLabel)
        val lineIndex = getLineIndex()
        if (lineIndex == null) {
            return null
        }
        val card = getCardFromColumn(lineIndex)
        if (card == null) {
            return null
        }
        return card
    }

    private fun promptPosition(promptMessage: String): Int? {
        var numberPosition = IO(promptMessage).get().trim().toIntOrNull()
        return if (numberPosition == 0 && FIX_POSITION_0_TO_1) 1 else numberPosition
    }

    private fun getLineIndex(promptMessage: String = LINE_LABEL): Int? {
        fun lineNumberValidator(lineNumber: Int) = (lineNumber in 1..this.cards.size)
        fun lineIndexGetter(lineNumber: Int) = (lineNumber - 1)
        fun lineIndexValidator(lineIndex: Any) = (hasFaceDownCards(lineIndex as Int))
        return getPositionalElementIfValid(
            promptMessage,
            ::lineNumberValidator,
            ::positionErrorMessage,
            ::lineIndexGetter,
            ::lineIndexValidator,
            ::lineFullyFaceUpErrorMessage
        ) as Int?
    }

    private fun getCardFromColumn(lineIndex: Int, promptMessage: String = COLUMN_LABEL): Card? {
        val lineBoard = this.cards[lineIndex]
        fun columnNumberValidator(columnNumber: Int) = (columnNumber in 1..lineBoard.size)
        fun cardGetter(columnNumber: Int) = this.cards[lineIndex][columnNumber - 1]
        fun cardValidator(card: Any) = ((card as Card).isFaceUp == false)
        return getPositionalElementIfValid(
            promptMessage,
            ::columnNumberValidator,
            ::positionErrorMessage,
            ::cardGetter,
            ::cardValidator,
            ::faceUpErrorMessage
        ) as Card?
    }

    private fun getPositionalElementIfValid(
        promptMessage: String,
        numberValidatorMethod: (number: Int) -> Boolean,
        numberErrorMessageMethod: (tryNumber: Int) -> String,
        elementGetterMethod: (positionNumber: Int) -> Any,
        elementValidatorMethod: (element: Any) -> Boolean,
        elementErrorMessageMethod: (tryNumber: Int) -> String
    ): Any? {
        for (tryNumber in 1..RETRIES_LIMIT_NUMBER) {
            var positionNumber = promptPosition(promptMessage)
            if (positionNumber == null || numberValidatorMethod(positionNumber) == false) {
                showErrorMessage(numberErrorMessageMethod(tryNumber))
                continue
            }
            val element = elementGetterMethod(positionNumber)
            if (elementValidatorMethod(element) == false) {
                showErrorMessage(elementErrorMessageMethod(tryNumber))
                continue
            }
            return element
        }
        return null
    }

    fun getMaxNumberOfColumns(): Int {
        var maxNumberOfColumns = Int.MIN_VALUE
        this.cards.forEach { if (it.size > maxNumberOfColumns) maxNumberOfColumns = it.size }
        return maxNumberOfColumns
    }

    // Método para renderizar (desenhar) o CardBoard na tela
    fun render(
        horizontalSeparator: String = "=",
        verticalSeparator: String = "|",
        cardWidth: Int = 7,
        cardHeight: Int = 4,
        cardFaceDownColor: String = "white",
        lineIndicatorWidth: Int = 3
    ) {

        val maxNumberOfColumns = getMaxNumberOfColumns()
        val fullWidth = (maxNumberOfColumns * cardWidth) + (maxNumberOfColumns + 2) + lineIndicatorWidth
        val columnSep = ComponentBuilder.Separator("-", fullWidth)
        val lineSep = ComponentBuilder.Separator(horizontalSeparator, fullWidth).toString()

        var cardBoardRender = " ".repeat(lineIndicatorWidth + 1) + verticalSeparator

        fun renderHeader() {
            val defaultWidth = DEFAULT_TERMINAL_WIDTH_SIZE
            val scoreBoardWidth = if (fullWidth > defaultWidth) fullWidth else defaultWidth
            val players = this.players.values.toTypedArray()
            val scoreBoard = ComponentBuilder.ScoreBoard(*players, width = scoreBoardWidth)
            println(scoreBoard)
        }

        fun getColumnsRender() {
            for (columnNumber in 1..maxNumberOfColumns) {
                val hasFaceDownCards = columnHasFaceDownCards(columnNumber - 1)
                var columnIndicator = if (hasFaceDownCards) {
                    columnNumber.toString().center(cardWidth).setBgColor("white").bold()
                } else {
                    " ".repeat(cardWidth)
                }
                cardBoardRender += columnIndicator + verticalSeparator
            }
            cardBoardRender += "\n" + columnSep + "\n"
        }

        renderHeader()
        getColumnsRender()

        val lineInMiddle = (if (cardHeight.isPair()) cardHeight.half() else cardHeight.half() + 1).toInt()

        // código que renderiza o tabuleiro por completo

        for (lineIndex in this.cards.indices) {
            val lineOnBoard = this.cards[lineIndex]
            val lineHasFaceDownCards = this.hasFaceDownCards(lineIndex)
            for (lineInRenderNumber in 1..cardHeight) {
                val lineInRenderIsInMiddle = lineInRenderNumber == lineInMiddle
                var lineIndicator = if (lineInRenderIsInMiddle && lineHasFaceDownCards) {
                    (lineIndex + 1).toString().center(lineIndicatorWidth).bold()
                } else {
                    " ".repeat(lineIndicatorWidth)
                }
                if (lineHasFaceDownCards) {
                    lineIndicator = lineIndicator.setBgColor("white")
                }

                var line = "$verticalSeparator$lineIndicator$verticalSeparator"
                for (card in lineOnBoard) {
                    val color = if (card.isFaceUp) card.color else cardFaceDownColor
                    val partialRender = when {
                        !card.isFaceUp -> ComponentBuilder.Square(color, cardWidth).toString()
                        lineInRenderIsInMiddle -> card.code.center(cardWidth).setBgColor(color).bold()
                        else -> ComponentBuilder.Square(color, cardWidth).toString()
                    }
                    line += partialRender + verticalSeparator
                }
                cardBoardRender += line + "\n"
            }
            cardBoardRender += lineSep + "\n"
        }

        // renderização do cardboard completo
        println(cardBoardRender)
    }


    // Métodos para obter mensagens de erro

    private fun faceUpErrorMessage() = "$FACEUP_ERROR_MESSAGE $FACEUP_RETRY_MESSAGE"
    private fun lineFullyFaceUpErrorMessage() = "$LINE_FULLY_FACEUP_ERROR_MESSAGE $POSITION_RETRY_MESSAGE"
    private fun positionErrorMessage() = "$POSITION_ERROR_MESSAGE $POSITION_RETRY_MESSAGE"

    private fun faceUpErrorMessage(tryNumber: Int): String {
        return if (isLastTry(tryNumber)) FACEUP_ERROR_MESSAGE else this.faceUpErrorMessage()
    }

    private fun lineFullyFaceUpErrorMessage(tryNumber: Int): String {
        return if (isLastTry(tryNumber)) LINE_FULLY_FACEUP_ERROR_MESSAGE else this.lineFullyFaceUpErrorMessage()
    }

    private fun positionErrorMessage(tryNumber: Int): String {
        return if (isLastTry(tryNumber)) POSITION_ERROR_MESSAGE else this.positionErrorMessage()
    }

    // Métodos auxiliares
    private fun isLastTry(tryNumber: Int) = (tryNumber >= RETRIES_LIMIT_NUMBER)
    private fun showErrorMessage(errorMessage: String) = println(errorMessage + "\n")

    // TODO ("Método utilizado para testes. Remover antes da entrega final")
    override fun toString(): String {
        var string = ""
        this.cards.forEach {
            it.forEach {
                string += it.toString() + "\n"
            }
            string += "\n"
        }
        return string
    }
}

class IO (val promptMessage: String) {
    private val blankValueErrorMessage = "Você precisa informar algo!"
    private val incorrectNumberErrorMessage = "Você precisa informar um número válido!"
    private val valueNotAllowedErrorMessage = "Você precisa informar um dos valores permitidos!"
    private val retryingMessage = "Vamos tentar mais uma vez..."

    /**
     * Mostra uma mensagem na tela e retorna o que o usuário digitar.
     * Este método não realiza nenhuma validação.
     */
    fun get(): String {
        print(this.promptMessage)
        return readln()
    }

    /**
     * Mostra uma mensagem na tela e retorna o que o usuário digitar.
     * Usa o método "get" como base, mas garante que o valor recebido por meio da entrada do usuário
     * não seja um valor em branco, como strings vazias ou preenchidas somente com caracteres
     * invisíveis (por exemplo: espaços, tabulações, quebras de linhas, etc...)
     * @param [errorMessage] (opcional)
     *    Mensagem de erro que será mostrada caso o usuário digite um valor em branco.
     *    Por padrão, utiliza a mensagem de erro definida pela classe para estes casos.
     *    No entanto, você pode mudar este comportamento por simplesmente fornecer a sua própria
     *    mensagem de erro como argumento para esse parâmetro.
     */
    fun getAny(errorMessage: String = this.blankValueErrorMessage): String {
        fun userInputIsNotBlank(userInput: Any): Boolean = !userInput.toString().isBlank()
        return getValidInput(String::class, ::get, ::userInputIsNotBlank, errorMessage)
    }

    /**
     * Mostra uma mensagem na tela e retorna o que o usuário digitar.
     * Usa o método "get" como base, mas garante que o valor recebido por meio da entrada do usuário
     * seja um dos valores permitidos por você.
     * @param [allowedValues] (obrigatório)
     *    Lista de valores permitidos (a entrada do usuário só será tratada como válida se o que ele
     *    digitar estiver listado aqui).
     * @param [strictCompare] (opcional)
     *    Se ativado, o método trabalhará com comparações estritas, diferenciando letras maiúsculas
     *    de minúsculas e considerando os espaçamentos. Se desativado, o método comparará somente o
     *    valor-bruto, ignorando se a letra está em caixa alta ou não e deixando de se importar com
     *    espaços. Por padrão, a comparação estrita é desativada.
     * @param [errorMessage] (opcional)
     *    Mensagem de erro que será mostrada caso o usuário digite um valor que não esteja listado
     *    no parâmetro "allowedValues".
     *    Por padrão, utiliza a mensagem de erro definida pela classe para estes casos.
     *    No entanto, você pode mudar este comportamento por simplesmente fornecer a sua própria
     *    mensagem de erro como argumento para esse parâmetro.
     */
    fun getFrom(
        allowedValues: List<String>,
        strictCompare: Boolean = false,
        errorMessage: String = this.valueNotAllowedErrorMessage
    ): String {
        fun userInputIsAllowed(value: Any): Boolean = if (strictCompare) {
            value.toString() in allowedValues
        } else {
            value.toString().trim().lowercase() in allowedValues.map { it.trim().lowercase() }
        }
        return getValidInput(String::class, ::get, ::userInputIsAllowed, errorMessage)
    }

    /**
     * Mostra uma mensagem na tela e retorna o que o usuário digitar.
     * Usa o método "getAny" como base, reaproveitando a validação que ele faz para impedir o
     * retorno de valores em branco. Já este método fica responsável por garantir que o valor
     * recebido por meio da entrada do usuário seja um número inteiro.
     * @param [errorMessage] (opcional)
     *    Mensagem de erro que será mostrada caso o usuário digite um valor que não represente um
     *    número inteiro.
     *    Por padrão, utiliza a mensagem de erro definida pela classe para estes casos.
     *    No entanto, você pode mudar este comportamento por simplesmente fornecer a sua própria
     *    mensagem de erro como argumento para esse parâmetro.
     */
    fun getInt(errorMessage: String = incorrectNumberErrorMessage): Int {
        fun userInputIsNumber(userInput: Any): Boolean = userInput.toString().toIntOrNull() != null
        return getValidInput(Int::class, ::getAny, ::userInputIsNumber, errorMessage)
    }

    /**
     * Mostra uma mensagem na tela e retorna o que o usuário digitar.
     * Este método é uma alternativa quando se deseja limitar o número de vezes que o usuário pode
     * digitar algo, evitando o comportamento padrão de loop infinito imposto pelos outros métodos
     * da classe. Usa o método "get" como base.
     * @param [retriesLimit] (obrigatório)
     *    Número de vezes que o usuário pode tentar digitar o valor correto. Caso ele extrapole,
     *    o método retornará null.
     * @param [errorMessage] (opcional)
     *    Mensagem de erro que será mostrada caso o usuário digite um valor que não represente um
     *    número inteiro.
     *    Por padrão, utiliza a mensagem de erro definida pela classe para estes casos.
     *    No entanto, você pode mudar este comportamento por simplesmente fornecer a sua própria
     *    mensagem de erro como argumento para esse parâmetro.
     *
     */
    fun getInt(retriesLimit: Int, errorMessage: String = incorrectNumberErrorMessage): Int? {
        for (numberTry in 1..retriesLimit) {
            val userInput = get().toIntOrNull()
            if (userInput is Int) {
                return userInput
            }
            if (numberTry < retriesLimit) {
                showErrorMessage(errorMessage)
            }
        }
        return null
    }

    /**
     * Mostra uma mensagem na tela e retorna o que o usuário digitar.
     * Usa o método "getInt" como base, reaproveitando a validação que ele usa do método "getAny"
     * (que impede que o valor capturado por meio da entrada do usuário seja um valor em branco) e
     * reaproveitando a validação que ele faz (de garantir que o valor recebido seja um número
     * inteiro). Já este método fica responsável por garantir que o número recebido seja um dos
     * números permitidos por você.
     * @param [allowedValues] (obrigatório)
     *    Lista de números permitidos (a entrada do usuário só será tratada como válida se o que ele
     *    digitar estiver listado aqui).
     * @param [errorMessage] (opcional)
     *    Mensagem de erro que será mostrada caso o usuário digite um número que não esteja listado
     *    no parâmetro "allowedValues".
     *    Por padrão, utiliza a mensagem de erro definida pela classe para estes casos.
     *    No entanto, você pode mudar este comportamento por simplesmente fornecer a sua própria
     *    mensagem de erro como argumento para esse parâmetro.
     */
    fun getIntFrom(
        allowedValues: List<Int>,
        errorMessage: String = this.valueNotAllowedErrorMessage
    ): Int {
        fun userInputIsAllowed(value: Any): Boolean = value.toString().toInt() in allowedValues
        return getValidInput(Int::class, ::getInt, ::userInputIsAllowed, errorMessage)
    }

    /**
     * (Privado! Não pode usar isso diretamente!)
     *
     * Método utilizado pela classe para retornar um valor obtido por meio da entrada do usuário,
     * no tipo desejado. Este método vai garantir que o valor obtido seja válido, obrigando o
     * usuário a tentar novamente enquanto ele não digitar algo que esteja dentro do esperado.
     * No final, retornará a entrada validada e convertida para o tipo desejado.
     * @param [targetType] (obrigatório)
     *    Tipo para o qual o valor deve ser convertido após passar pela validação.
     * @param [methodForPrompt] (obrigatório)
     *    Método a ser usado para pedir a informação para o usuário. Esta classe, por exemplo,
     *    disponibiliza os métodos "get", "getAny", "getFrom", "getInt" e "getIntFrom". O que é
     *    passado aqui é a referência para o método desejado para que ele possa ser chamado dentro
     *    deste método.
     * @param [methodForValidation] (obrigatório)
     *    Método a ser usado para verificar o valor recebido por meio da entrada do usuário e
     *    verificar se isso está OK. Assim como o parâmetro "methodForPrompt", o que é passado aqui
     *    é a referência para o método que deve ser utilizado para verificar o valor obtido.
     * @param [errorMessage] (obrigatório)
     *    Mensagem de erro que deve ser usada caso a entrada digitada pelo usuário não seja válida.
     * */
    private fun <ReturnType: Any>getValidInput(
        targetType: KClass<ReturnType>,
        methodForPrompt: () -> Any,
        methodForValidation: (Any) -> Boolean,
        errorMessage: String,
    ): ReturnType {
        val userInput = promptWhileNotValid(methodForPrompt, methodForValidation, errorMessage)
        return convert<ReturnType>(userInput, targetType)
    }

    /**
     * (Privado! Não pode usar isso diretamente!)
     *
     * Método utilizado pela classe (no método "getValidInput"), para prender o usuário num loop e
     * forçá-lo a digitar algo que seja válido. Este método garante o retorno de um valor válido,
     * que foi obtido por meio da entrada do usuário, que foi validado previamente e que pode ser
     * usado sem problemas durante a execução do programa.
     * @param [methodForPrompt] (obrigatório)
     *    Método a ser usado para pedir a informação para o usuário. Esta classe, por exemplo,
     *    disponibiliza os métodos "get", "getAny", "getFrom", "getInt" e "getIntFrom". O que é
     *    passado aqui é a referência para o método desejado para que ele possa ser chamado dentro
     *    deste método.
     * @param [methodForValidation] (obrigatório)
     *    Método a ser usado para verificar o valor recebido por meio da entrada do usuário e
     *    verificar se isso está OK. Assim como o parâmetro "methodForPrompt", o que é passado aqui
     *    é a referência para o método que deve ser utilizado para verificar o valor obtido.
     * @param [errorMessage] (obrigatório)
     *    Mensagem de erro que deve ser usada caso a entrada digitada pelo usuário não seja válida.
     */
    private fun promptWhileNotValid(
        methodForPrompt: () -> Any,
        methodForValidation: (userInput: Any) -> Boolean,
        errorMessage: String
    ): Any {
        var userInput: Any
        var userInputIsValid: Boolean
        do {
            userInput = methodForPrompt()
            userInputIsValid = methodForValidation(userInput)
            if (!userInputIsValid) {
                showErrorMessage(errorMessage)
            }
        } while (!userInputIsValid)
        return userInput
    }

    /**
     * (Privado! Não pode usar isso diretamente!)
     *
     * Método utilizado pela classe para exibir mensagens de erro na tela quando o usuário digita
     * um valor inválido. Este método automaticamente concatena a mensagem de erro com a mensagem de
     * nova tentativa para exibir a mensagem por completo na tela, além de inserir uma quebra de
     * linha após isso para que o usuário possa iniciar uma nova tentativa.
     * @param [errorMessage] (obrigatório)
     *    Mensagem de erro que deve ser exibida na tela.
     */
    private fun showErrorMessage(errorMessage: String) {
        println("$errorMessage ${this.retryingMessage}\n")
    }

    /**
     * (Privado! Não pode usar isso diretamente!)
     *
     * Método utilizado pela classe para converter valores.
     * Somente converte para String ou para Int, pois esses são os únicos tipos usados pela classe.
     * @param [value] (obrigatório)
     *    Valor que será convertido.
     * @param [targetType] (obrigatório)
     *    Tipo para o qual o valor será convertido.
     * @throws [ClassCastException] Disparado caso o tipo escolhido para conversão seja algo além de
     * String e Int.
     */
    private fun <newType: Any> convert(value: Any, targetType: KClass<newType>): newType {
        return when (targetType) {
            String::class -> value.toString() as newType
            Int::class -> value.toString().toInt() as newType
            else -> throw ClassCastException(
                "Não pode converter para o tipo $targetType, apenas para String ou Int!"
            )
        }
    }
}


/* *************************************************************************************************
*                                   FUNÇÃO AUXILIARES
*   Funções de reaproveitamento utilizadas por mais de uma parte do código
***************************************************************************************************/

fun repeatDiv(length: Int = DEFAULT_TERMINAL_WIDTH_SIZE){
    println("=".repeat(length))
}
fun showRules() {
    repeatDiv()
    println("                             $GAME_NAME      ")
    repeatDiv()
    println(   "REGRAS DO JOGO "  )

<<<<<<< HEAD
fun quadradoRed(size: Int = 2){
    val red = "\u001B[41m" //vermelho
    val reset = "\u001B[0m" // padrão

    for (i in 1..size){
        print(red + " ".repeat(size) + reset)
    }
}

// Função criada para a exibição de quadrado da cor azul
fun quadradoBlue(size: Int = 2) {
    val blue = "\u001B[44m" // Fundo azul
    val reset = "\u001B[0m" // Reset de cor

    for (i in 1..size) {
        print(blue + " ".repeat(size) + reset)
    }
}

=======
    println("1. Cada participante deve ter atribuído a si uma cor (vermelho ou azul) no início do\n" +
            "jogo.")
    println("2. Todo participante deve ter um nome registrado. Senão, o nome padrão\n" +
            "“PARTICIPANTE01” e “PARTICIPANTE02” devem ser atribuídos a cada um das(os)\n" +
            "participantes.")
    println("3. Cada participante possui uma pontuação atrelada a si.")
    println("4. Se a(o) participante encontrar um par de cartas com fundo amarelo, fatura 1\n" +
            "ponto.")
    println("5. Se a(o) participante encontrar um par de cartas com o fundo da sua cor, fatura 5\n" +
            "pontos.")
    println("6. Se a(o) participante encontrar um par de cartas com o fundo da cor de seu\n" +
            "adversário e errar, perde 2 pontos. Porém, se acertar, ganha apenas 1 ponto.")
    println("7. RA(o) participante não pode ter pontuação negativa. Se ela(ele) perder mais\n" +
            "pontos do que possui, ficará com a pontuação zerada")
    println("8. Se a(o) participante encontrar uma carta com o fundo preto e errar o seu par,\n" +
            "perde o jogo, mesmo que tenha a pontuação superior à da(o) outra(o)\n" +
            "participante. Mas se acertar, ganha o jogo.")

}
>>>>>>> dc05d19289b4fd1c7405a5eeb111de771bc32920
/* *************************************************************************************************
*                                   FUNÇÃO DE CONFIGURAÇÃO DO JOGO
*   Função responsável por receber as informações do jogador e configurar o tabuleiro de cartas.
*   Ela entrega o jogo todo configurado para a função responsável por controlar o fluxo do jogo
***************************************************************************************************/

fun configGame(): Pair<CardBoard, Map<String, Player>> {



    /**
     * Função criada para a exibição de quadrado da cor vermelha
     */
    fun quadradoRed(size: Int = 2){
        val red = "\u001B[41m" //vermelho
        val reset = "\u001B[0m" // padrão

        for (i in 1..size){
            print(red + " ".repeat(size) + reset)
        }
    }

    // Função criada para a exibição de quadrado da cor azul
    fun quadradoBlue(size: Int = 2) {
        val blue = "\u001B[44m" // Fundo azul
        val reset = "\u001B[0m" // Reset de cor

        for (i in 1..size) {
            print(blue + " ".repeat(size) + reset)
        }
    }

    // função para impedir que o usuário insira um apelido muito longo
    fun limiteCaracter(mensagem: String, maxCaracter: Int = 16, player: Int): String {
        var inputApelido: String
        do {
            inputApelido = IO(mensagem).get().takeIf { it.isNotBlank() } ?: if (player == 1) "PARTICIPANTE01" else "PARTICIPANTE02"
            if (inputApelido.length > maxCaracter) {
                println("O apelido não pode exceder $maxCaracter caracteres. Tente novamente.")
            }
        } while (inputApelido.length > maxCaracter)
        return inputApelido
    }

    var ultimaCor: Int? = null
    /**
     * Função que atribui aleatoriamente uma cor para o usuário
     * 1 - cor vermelha
     * 2 - cor azul
     * **/
    fun jogadorCor(): String{
        var corSorteada = (1..2).random()


        while (corSorteada == ultimaCor){
            corSorteada = (1..2).random()
        }

        val cor = when(corSorteada){
            1 -> "red"
            2 -> "blue"
            else -> "red"
        }

        ultimaCor = corSorteada
        return cor
    }

    /**
     * recebe uma string no formato "NxN" (por exemplo, "4x4", "6x6", etc.)
     * e retorna um Pair<Int, Int>, onde o primeiro valor é o número de linhas
     * o segundo valor é o número de colunas.
     * **/
    fun parseBoardSize(size: String): Pair<Int, Int> {
        val parts = size.split("x")
        if (parts.size != 2) {
            throw IllegalArgumentException("Formato de tamanho do tabuleiro inválido. Use o formato 'NxN'.")
        }
        val lines = parts[0].toIntOrNull() ?: throw IllegalArgumentException("Número de linhas inválido.")
        val columns = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Número de colunas inválido.")
        return Pair(lines, columns)
    }



    // Header da configuração
    println()
    repeatDiv()
    println("                                  CONFIGURAÇÕES DO JOGO                        ")
    repeatDiv()

    println()

    // Configuração do tabuleiro
    repeatDiv()
    repeatDiv()
    println("QUAL O TAMANHO DE TABULEIRO DESEJA JOGAR?")
    repeatDiv()
    println("    a. 4x4")
    print("    ")
    repeatDiv(86)
    println("    b. 6x6")
    print("    ")
    repeatDiv(86)
    println("    c. 8x8")
    print("    ")
    repeatDiv(86)
    println("    d. 10x10")
    print("    ")
    repeatDiv(86)

    // Leitura da opção do tabuleiro
    val tamanhoTab = IO("DIGITE A OPÇÃO: ").getFrom(listOf("a", "b", "c", "d"))
    val tamanhoFormatado = when (tamanhoTab) {
        "a" -> "4x4"
        "b" -> "6x6"
        "c" -> "8x8"
        "d" -> "10x10"
        else -> "4x4"
    }
    repeatDiv()
    println()

    // Atribuição de apelidos
    repeatDiv()
    println("QUAL O APELIDO DA(O) PARTICIPANTE 1? ")
    repeatDiv()

    val nomePlayer1 = limiteCaracter("DIGITE O APELIDO: ", player = 1)
    repeatDiv()

    var nomePlayer2: String

    do {
        repeatDiv()
        println("QUAL O APELIDO DA(O) PARTICIPANTE 2? ")
        repeatDiv()
        nomePlayer2 = limiteCaracter("DIGITE O APELIDO: ", player = 2)

        if (nomePlayer2.equals(nomePlayer1, ignoreCase = true)) {
            println("Os apelidos não podem ser iguais. Por favor, escolha outro apelido.")
        }
    } while (nomePlayer2.equals(nomePlayer1, ignoreCase = true))

    repeatDiv()
    println()

    // Exibição das cores dos jogadores
    repeatDiv()
    println("                               $GAME_NAME                        ")
    repeatDiv()

    val corPlayer1 = jogadorCor()
    print("${nomePlayer1} - ")
    when (corPlayer1) {
        "red" -> quadradoRed()
        "blue" -> quadradoBlue()
    }

    println()
    println()

    val corPlayer2 = jogadorCor()
    print("${nomePlayer2} - ")
    when (corPlayer2) {
        "red" -> quadradoRed()
        "blue" -> quadradoBlue()
    }

    println()

    // Geração do tabuleiro
    val (linhas, colunas) = parseBoardSize(tamanhoFormatado)
    val cardBoard = CardBoard(linhas, colunas)

    // Configuração dos jogadores
    val playersInfo = mapOf(
        corPlayer1 to Player(corPlayer1, nomePlayer1),
        corPlayer2 to Player(corPlayer2, nomePlayer2)
    )

    // Retorna o tabuleiro e o mapa de jogadores
    return Pair(cardBoard, playersInfo)
}

/* *************************************************************************************************
*                                   FUNÇÃO PARA INICIAR O JOGO
*   Função responsável por controlar todo o fluxo do jogo. É ela que cuida da funcionadalide
*   principal deste programa, jogar!
***************************************************************************************************/

fun runGame(cardBoard: CardBoard, playersInfo: PlayersMap) {

    fun getCardsLabel(): Array<String> {
        val cardsLabel = mutableListOf<String>()
        for (numberOfCard in 1..NUMBER_OF_COPIES_PER_CARD) {
            cardsLabel.add(cardLabel(numberOfCard))
        }
        return cardsLabel.toTypedArray()
    }

    fun findNextPlayerInMap(actualPlayerColor: String, playersInfo: PlayersMap): Player {
        val playersColor = playersInfo.keys.toList()
        val actualPlayerColorIndex = playersColor.indexOf(actualPlayerColor)
        val actualPlayerIsLast = (actualPlayerColorIndex == playersColor.lastIndex)
        val nextPlayerColorIndex = if (actualPlayerIsLast) 0 else actualPlayerColorIndex + 1
        val nextPlayerColor = playersColor[nextPlayerColorIndex]
        val nextPlayer = playersInfo[nextPlayerColor]
        return nextPlayer as Player
    }

    fun containsEnemyCard(ownColor: ColorString, colorsFound: Set<ColorString>): Boolean {
        colorsFound.forEach { if ( (it !in SPECIAL_COLORS) && (it != ownColor)) return true }
        return false
    }

    fun evalSelection(playerColor: String, cards: List<Card>, playersInfo: PlayersMap): Boolean {
        val player = playersInfo[playerColor] as Player
        val codesFound = mutableSetOf<String>()
        val colorsFound = mutableSetOf<ColorString>()

        cards.forEach { codesFound.add(it.code); colorsFound.add(it.color) }

        val gotCardsWithSameColor = colorsFound.size == 1
        val gotCardsWithsSameCode = codesFound.size == 1
        val gotAllCopiesOfCard = (gotCardsWithsSameCode) && (gotCardsWithSameColor)
        val gotOwnColor = colorsFound.first() == playerColor
        val gotBlackCard = "black" in colorsFound
        val gotEnemyCard = containsEnemyCard(playerColor, colorsFound)
        if (gotAllCopiesOfCard) {
            val pointsEarned = when {
                gotBlackCard -> BLACK_CARD_POINTS
                gotOwnColor -> OWN_CARD_POINTS
                else -> 1
            }
            player.upScore(pointsEarned)
            ActionStatus.printSuccess(pointsEarned)
            return true
        } else {
            val nextPlayer = findNextPlayerInMap(playerColor, playersInfo)
            val lostPoints = when {
                gotBlackCard -> BLACK_CARD_POINTS
                gotEnemyCard -> DEFAULT_LOST_POINTS
                else -> 0
            }
            if (lostPoints == 0) {
                ActionStatus.printNothingHappens(nextPlayer)
            } else {
                ActionStatus.printWrong(lostPoints, nextPlayer)
            }
            player.downScore(lostPoints)
            cards.forEach { it.isFaceUp = false }
            return false
        }
        readln()
    }

    cardBoard.players = playersInfo
    while (cardBoard.hasFaceDownCards()) {
        for ((color, player) in playersInfo.entries) {
            var playerMustContinue: Boolean = false
            do {
                cardBoard.render()
                val playerLabel = "$PLAYER_LABEL${player.nickname}"
                println(playerLabel)
                val selectedCards = cardBoard.getCards(*getCardsLabel())
                if (selectedCards == null) {
                    ActionStatus.printRetryExceeded(findNextPlayerInMap(color, playersInfo))
                    continue
                }
                playerMustContinue = evalSelection(color, selectedCards, playersInfo)
                println()
            } while (playerMustContinue && cardBoard.hasFaceDownCards())
            if (!cardBoard.hasFaceDownCards()) {
                break
            }
        }
    }
    ActionStatus.printWinner(playersInfo.values.toList())
}

fun showRanking(playersInfo: Map<String, Player>) {
    // Verifica se há jogadores para exibir
    if (playersInfo.isEmpty()) {
        println("Nenhum jogador cadastrado.")
        return
    }

    // Converte o mapa de jogadores em uma lista ordenada por pontuação (do maior para o menor)
    val ranking = playersInfo.values.sortedByDescending { it.score }

    // Exibe o ranking
    repeatDiv()
    println("RANKING DOS JOGADORES".center())
    repeatDiv()
    ranking.forEachIndexed { index, player ->
        // Exibe a posição, o nome do jogador e o quadrado colorido
        print("${index + 1}º Lugar: ${player.nickname} - ")
        when (player.color) {
            "red" ->  quadradoRed()
                "blue" -> quadradoBlue()
            else -> print("██") // Quadrado padrão (caso a cor não seja reconhecida)
        }
        println(" ${player.score} pontos")
    }
}

fun main(){
    // Váriavel para armazenar informações dos jogadores
    var playersInfo: Map<String, Player>? = null

    // Menu em looping Infinito, até ser encerrado em alguma condição.
    while (true){
        // Header da aplicação
        repeatDiv()
        println("                                  Manga Rosa Memory Game          ")
        repeatDiv()

        // Opções do menu
        println("1. INICIAR")
        println("2. PONTUAÇÃO PARTICIPANTES")
        println("3. REGRAS DO JOGO")
        println("4. SAIR")
        repeatDiv()

        /**
         * Criação de váriavel que armazena valor da opção desejada pelo usuário.
         * Utilização da classe IO para tratamento de valores.
         * **/
        val opcaoEscolhida = IO("INFORME SUA OPÇÃO: ").getIntFrom(listOf(1, 2, 3, 4))

        when(opcaoEscolhida){
            1 -> {
                //
                val (cardBoard, players) = configGame()
                playersInfo = players
                runGame(cardBoard, players)
            }
            2 -> {
                if (playersInfo != null) {
                    showRanking(playersInfo)
                } else {
                    println("Nenhum jogo foi iniciado ainda. Por favor, inicie um jogo primeiro.")
                }
            }
            3 -> {
                showRules()
            }
            4 -> {
                println("ATÉ MAIS!")
                return
            }
        }

    }

}