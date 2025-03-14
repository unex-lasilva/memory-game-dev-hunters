/**
 * Módulo principal da aplicação
 * Contém a função main que irá chamar todos os outros métodos desenvolvidos
 * **/

package br.mangarosa.memorygame

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
                //runGame(cardBoard, players)
            }
            2 -> {
                TODO("necessário implementação!")

            }
            3 -> {
                TODO("necessário implementação")
                //showRules()
            }
            4 -> {
                println("Que pena que está encerrando a jogatina. Até mais!")
                return
            }
        }

    }

}