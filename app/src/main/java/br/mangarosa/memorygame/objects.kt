package br.mangarosa.memorygame

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