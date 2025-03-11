package br.mangarosa.memorygame

const val GAME_NAME = "MANGA ROSA MEMORY GAME"

const val NUMBER_OF_COPIES_PER_CARD = 2
const val NUMBER_OF_BLACK_CARDS = 1
const val BLACK_CARD_POINTS = 50
const val RED_CARDS_AMOUNT_PCT = 0.25f
const val BLUE_CARDS_AMOUNT_PCT = 0.25f

const val RETRIES_LIMIT_NUMBER = 3
const val FIX_POSITION_0_TO_1 = true

const val LINE_LABEL = "LINHA: "
const val COLUMN_LABEL = "COLUNA: "

typealias ColorString = String
typealias LineOfCards = List<Card>

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

    fun getScoreLabel(): String {
        return if (this.score in 0..1) "${this.score} ponto" else "${this.score} pontos"
    }

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

    fun getCard(lineIndex: Int, columnIndex: Int): Card {
        return this.cards[lineIndex][columnIndex]
    }

    fun hasFaceUpCards(): Boolean {
        this.cards.forEach { it.forEach { if (it.isFaceUp) return true } }
        return false
    }

    fun hasFaceUpCards(lineIndex: Int): Boolean {
        this.cards[lineIndex].forEach { if (it.isFaceUp) return true }
        return false
    }

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