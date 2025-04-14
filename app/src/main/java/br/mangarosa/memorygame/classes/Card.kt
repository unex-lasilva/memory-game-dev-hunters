package br.mangarosa.memorygame.classes

import java.io.Serializable

class Card(val code: String, val color: String, var isFaceUp: Boolean = false) : Serializable