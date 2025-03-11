/**
 * Módulo principal da aplicação
 * Contém a função main que irá chamar todos os outros métodos desenvolvidos
 * **/

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
                runGame(cardBoard, players)
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