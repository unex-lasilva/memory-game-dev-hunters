package br.mangarosa.memorygame

fun runGame(cardBoard: CardBoard, playersInfo: PlayersMap) {

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