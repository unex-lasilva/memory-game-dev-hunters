package br.mangarosa.memorygame

import android.health.connect.datatypes.units.Length

/**
 * Esse módulo é responsável por prover a capacidade de configuração do jogo.
 * Precisa implementar a função principal deste módulo. É ela que será a responsável por desenhar a
 * tela de configuração no terminal e permitir que o usuário configure o jogo da forma que ele
 * quiser.
 *
 * Funções auxiliares podem ser criadas para ajudar na implementação.
 * Na verdade... é até recomendável!
 *
 * @author [coloque o seu nome e depois remova os colchetes]
 */

/**
 * Esta é a função responsável pela funcionalidade de configurar o jogo.
 * Precisa desenhar a tela conforme o esboço mostrado na OAT e também seguir à risca as
 * especificidades definidas por lá. Deixei anotado para você um passo a passo resumido do que deve
 * acontecer nesta etapa...
 * - Etapa 1: Escolher o tamanho do tabuleiro (4x4, 6x6, 8x8, 10x10)
 * - Etapa 2: Escolher o apelido do participante 1.
 *            (se nada for escolhido, o padrão é "PARTICIPANTE01").
 * - Etapa 3: Escolher o apelido do participante 2.
 *            (se nada for escolhido, o padrão é "PARTICIPANTE01").
 *   (Garanta que o apelido dos dois participantes não sejam iguais)
 *
 * @return É esperado que seja retornado uma lista contendo o resultado de cada configuração feita,
 * na seguinte ordem: tamanho do tabuleiro, apelido do participante 1 e apelido do participante 2.
 * Essa é a ordenação que a função para rodar o jogo usará para importar as configurações feitas
 * pelo usuário e interpretá-las da forma correta.
 *
 * OBS: Retorne o tamanho do tabuleiro no mesmo formato usado para mostrá-lo na tela de opções.
 * Por exemplo: "4x4" deve ser retornado quando o tabuleiro 4x4 é escolhido.
 */

/**
 * Função criada utilizando repeat para facilitar o uso de divisão e deixar o código mais legível.
 */

fun repeatDiv(length: Int = 90){
    println("=".repeat(length))
}

/**
 * Função criada para a exibição de quadrado da cor vermelha
 */
fun quadradoRed(size: Int = 2){
    val red = "\u001B[41m" //vermelho
    val reset = "\u001B[0m" // padrão

    for (i in 1..size){
        print(red + " ".repeat(size) + reset)
    }
}

// Função criada para a exibição de quadrado da cor azul
fun quadradoBlue(size: Int = 2) {
    val blue = "\u001B[44m" // Fundo azul
    val reset = "\u001B[0m" // Reset de cor

    for (i in 1..size) {
        print(blue + " ".repeat(size) + reset)
    }
}

// função para impedir que o usuário insira um apelido muito longo
fun limiteCaracter(mensagem: String, maxCaracter: Int = 16, player: Int): String {
    var inputApelido: String
    do {
        inputApelido = IO(mensagem).get().takeIf { it.isNotBlank() } ?: if (player == 1) "PARTICIPANTE01" else "PARTICIPANTE02"
        if (inputApelido.length > maxCaracter) {
            println("O apelido não pode exceder $maxCaracter caracteres. Tente novamente.")
        }
    } while (inputApelido.length > maxCaracter)
            return inputApelido
}



var ultimaCor: Int? = null
/**
 * Função que atribui aleatoriamente uma cor para o usuário
 * 1 - cor vermelha
 * 2 - cor azul
 * **/
fun jogadorCor(): String{
    var corSorteada = (1..2).random()


    while (corSorteada == ultimaCor){
        corSorteada = (1..2).random()
    }

    val cor = when(corSorteada){
        1 -> "red"
        2 -> "blue"
        else -> "red"
    }

    ultimaCor = corSorteada
    return cor
}

fun configGame(): Pair<List<List<Card>>, Map<String, Player>> {
    // Header da configuração
    println()
    repeatDiv()
    println("                                  CONFIGURAÇÕES DO JOGO                        ")
    repeatDiv()

    println()

    // Configuração do tabuleiro
    repeatDiv()
    repeatDiv()
    println("QUAL O TAMANHO DE TABULEIRO DESEJA JOGAR?")
    repeatDiv()
    println("    a. 4x4")
    print("    ")
    repeatDiv(86)
    println("    b. 6x6")
    print("    ")
    repeatDiv(86)
    println("    c. 8x8")
    print("    ")
    repeatDiv(86)
    println("    d. 10x10")
    print("    ")
    repeatDiv(86)

    // Leitura da opção do tabuleiro
    val tamanhoTab = IO("DIGITE A OPÇÃO: ").getFrom(listOf("a", "b", "c", "d"))
    val tamanhoFormatado = when (tamanhoTab) {
        "a" -> "4x4"
        "b" -> "6x6"
        "c" -> "8x8"
        "d" -> "10x10"
        else -> "4x4"
    }
    repeatDiv()
    println()

    // Atribuição de apelidos
    repeatDiv()
    println("QUAL O APELIDO DA(O) PARTICIPANTE 1? ")
    repeatDiv()

    val nomePlayer1 = limiteCaracter("DIGITE O APELIDO: ", player = 1)
    repeatDiv()

    var nomePlayer2: String

    do {
        repeatDiv()
        println("QUAL O APELIDO DA(O) PARTICIPANTE 2? ")
        repeatDiv()
        nomePlayer2 = limiteCaracter("DIGITE O APELIDO: ", player = 2)

        if (nomePlayer2.equals(nomePlayer1, ignoreCase = true)) {
            println("Os apelidos não podem ser iguais. Por favor, escolha outro apelido.")
        }
    } while (nomePlayer2.equals(nomePlayer1, ignoreCase = true))

    repeatDiv()
    println()

    // Exibição das cores dos jogadores
    repeatDiv()
    println("                               MANGA ROSA MEMORY GAME                        ")
    repeatDiv()

    val corPlayer1 = jogadorCor()
    print("${nomePlayer1} - ")
    when (corPlayer1) {
        "red" -> quadradoRed()
        "blue" -> quadradoBlue()
    }

    println()
    println()

    val corPlayer2 = jogadorCor()
    print("${nomePlayer2} - ")
    when (corPlayer2) {
        "red" -> quadradoRed()
        "blue" -> quadradoBlue()
    }

    // Geração do tabuleiro
    val (linhas, colunas) = getNumberOfLinesAndColumns(tamanhoFormatado)
    val cardBoard = getCardsForGame(linhas, colunas)

    // Configuração dos jogadores
    val playersInfo = mapOf(
        corPlayer1 to Player(nomePlayer1, 0),
        corPlayer2 to Player(nomePlayer2, 0)
    )

    // Retorna o tabuleiro e o mapa de jogadores
    return Pair(cardBoard, playersInfo)
}

fun main() {
    configGame()
}