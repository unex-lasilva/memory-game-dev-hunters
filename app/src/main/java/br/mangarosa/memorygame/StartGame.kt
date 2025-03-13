/**
 * Módulo principal da aplicação
 * Contém a função main que irá chamar todos os outros métodos desenvolvidos
 * **/

package br.mangarosa.memorygame

const val GAME_NAME = "MANGA ROSA MEMORY GAME"

const val NUMBER_OF_COPIES_PER_CARD = 2
const val NUMBER_OF_BLACK_CARDS = 1
const val BLACK_CARD_POINTS = 50
const val RED_CARDS_AMOUNT_PCT = 0.25f
const val BLUE_CARDS_AMOUNT_PCT = 0.25f
const val RETRIES_LIMIT_NUMBER = 3
const val FIX_POSITION_0_TO_1 = true

const val COLUMN_LABEL = "COLUNA: "
const val LINE_LABEL = "LINHA: "
const val PLAYER_LABEL = "PARTICIPANTE: "

const val FACEUP_ERROR_MESSAGE = "A carta da posição informada já está virada."
const val LINE_FULLY_FACEUP_ERROR_MESSAGE = "Todas as cartas desta linha já estão viradas."
const val POSITION_ERROR_MESSAGE = "Posição da carta inválida."

const val FACEUP_RETRY_MESSAGE = "Por favor, escolha outra posição!"
const val POSITION_RETRY_MESSAGE = "Por favor, insira uma posição válida!"

typealias CardsCollection = MutableList<Card>
typealias ColorString = String
typealias LineOfCards = List<Card>
typealias PlayersMap = Map<ColorString, Player>

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