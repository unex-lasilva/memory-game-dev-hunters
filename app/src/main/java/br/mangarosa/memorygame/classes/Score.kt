package br.mangarosa.memorygame.classes

import java.io.Serializable

class Score(points: Int) : Serializable {
    var points = returnOwnIfValidOrZero(points)
        set(value) { field = returnOwnIfValidOrZero(value) }

    fun up(pointsToAdd: Int = 1) {
        this.points += pointsToAdd
    }

    fun down(pointsToDecrease: Int = 1) {
        this.points -= pointsToDecrease
    }

    override fun toString(): String {
        val label = if (this.points in listOf(-1, 1)) "ponto" else "pontos"
        return "${this.points} $label"
    }

    private fun returnOwnIfValidOrZero(points: Int): Int =  if (points >= 0) points else 0
}