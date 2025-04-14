package br.mangarosa.memorygame.classes

import java.io.Serializable
import kotlin.random.Random

typealias CardsCollection = MutableList<Card>
typealias ColorString = String
typealias LineOfCards = List<Card>

class CardBoard(val lines: Int, val columns: Int) : Serializable {
    val NUMBER_OF_COPIES_PER_CARD = 2
    val NUMBER_OF_BLACK_CARDS = 1

    val RED_CARDS_AMOUNT_PCT = 0.25f
    val BLUE_CARDS_AMOUNT_PCT = 0.25f

    val BLACK_CARD_POINTS = 50
    val OWN_CARD_POINTS = 5
    val DEFAULT_LOST_POINTS = 2


    val cards = mutableListOf<LineOfCards>()

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
}