package br.mangarosa.memorygame.classes

import java.io.Serializable

class Player(val color: String, val nickname: String, val score: Score = Score(0)) : Serializable {
    override fun toString() = "${this.nickname} - ${this.score}"
}