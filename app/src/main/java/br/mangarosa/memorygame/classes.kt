package br.mangarosa.memorygame

import kotlin.random.Random
import kotlin.reflect.KClass

class Card(val code: String, val color: String, var isFaceUp: Boolean = false) {
    // TODO ("Método utilizado para testes. Remover antes da entrega final")
    override fun toString(): String {
        return "[${this.code}] Color: ${this.color}; Is face-up? ${this.isFaceUp}"
    }
    // TODO ("Método utilizado para testes. Remover antes da entrega final")
    fun toString(line: Int, column: Int): String {
        return "[${line}/${column}] ${toString()}"
    }
}

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

    // TODO ("Método utilizado para testes. Remover antes da entrega final")
    override fun toString(): String {
        return "[${this.color}] ${this.nickname}: ${this.score}"
    }
    // TODO ("Método utilizado para testes. Remover antes da entrega final")
    fun toString(playerNumber: Int, totalPlayersNumber: Int): String {
        return "[$playerNumber/$totalPlayersNumber] ${toString()}"
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

/**
 * Esta classe disponibiliza métodos para lidar com a entrada do usuário de forma aprimorada.
 * Os métodos desta classe utilizam por debaixo dos panos funções que provavelmente já foram usadas
 * por você, como "readln()" e "readLine()", que capturam a entrada do usuário. O que os métodos
 * desta classe fazem é adicionar uma camada de validação para garantir que o valor digitado pelo
 * usuário seja o que você realmente espera. Isso quer dizer que: enquanto o usuário não digitar um
 * valor válido, ele não poderá prosseguir, sendo obrigado a tentar novamente quantas vezes
 * for necessário.
 *
 * Existem 5 métodos aqui criados para você usar e para facilitar a sua vida.
 * Vou fazer um resumo de cada um deles, mas você pode ver mais informações acessando a
 * documentação de cada método dentro da sua própria IDE...
 * - get: mostra a mensagem na tela e retorna a string do que o usuário digitar.
 * - getAny: mesmo que o "get", mas OBRIGATORIAMENTE o usuário tem que informar algo.
 * - getFrom: mesmo que o "get", mas o usuário TEM QUE digitar um dos valores permitidos por você.
 * - getInt: mesmo que o "get", mas o que o usuário digitar é retornado como um valor Int
 *           (portanto, ele OBRIGATORIAMENTE tem que digitar um número inteiro válido.)
 * - getIntFrom: mesmo que o "getInt", mas o usuário TEM QUE digitar um dos números permitidos por
 *           você.
 *
 * (Tenha em mente que a mensagem mostrada na tela é definida por você ao criar uma instância IO)
 *
 * Exemplo de uso:
 * - val numero = IO("Insira um número de 1-3: ").getIntFrom(listOf(1, 2, 3))
 *   (obrigatoriamente será do tipo Int e o número guardado pode ser 1, 2, ou 3)
 *
 * - val stringQualquer = IO("Digite algo: ").get()
 *   (guarda uma String qualquer)
 *
 * - val stringQualquer = IO("Digite algo (não deixe em branco): ").getAny()
 *   (guarda uma String qualquer que não pode ficar em branco)
 *
 * @param [promptMessage]
 *    Mensagem que aparecerá na tela antes de capturar a entrada do usuário.
 *    Deve avisar o usuário sobre que tipo de informação é esperado que ele digite.
 *
 * @property [blankValueErrorMessage]
 *    Mensagem de erro padrão quando o valor recebido está em branco.
 *    Ex: uma string vazia ou contendo vários espaços em branco, mas somente espaços em branco.
 *
 * @property [incorrectNumberErrorMessage]
 *    Mensagem de erro padrão quando o valor recebido não puder ser representado como um número.
 *    (OBS: Esta classe foi implementada de forma a lidar somente com valores inteiros.
 *          Números de ponto flutuante também serão tratados como incorretos.)
 *
 * @property [valueNotAllowedErrorMessage]
 *    Mensagem de erro padrão quando o valor recebido não for um dos valores desejados.
 *
 * @property [retryingMessage]
 *    Mensagem padrão para indicar ao usuário que ele deve fazer uma nova tentativa.
 *    (OBS: Quando o usuário digita algo inválido, automaticamente os métodos desta classe
 *          unem esta mensagem com a mensagem de erro definida para exibir o erro por completo na
 *          tela.)
 *
 * @author Denilson Santos
 */
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