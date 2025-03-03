import kotlin.io.readln;
import kotlin.random.Random

val jogadores = mutableMapOf<String, Map<String, Any>>()
var nomeJogador1: String = "PARTICIPANTE01"
var nomeJogador2: String = "PARTICIPANTE02"
val cores = listOf(
    "\u001B[31m", // Vermelho
    "\u001B[34m", // Azul
    "\u001B[33m", // Amarelo
    "\u001B[30m"  // Preto
)

val reset = "\u001B[0m"

// Matheus
fun main() {
    // Nessa função você pode adicionar a lógica de menu principal do jogo, como iniciar o jogo, exibir pontuações, regras, e as demais opções, chamando seus respectivos metodos.
    // Lembre-se de chamar a função config() para configurar o jogo antes de iniciar o menu principal.
    // Exemplo de menu:
    /*==============================================
               MANGA ROSA MEMORY GAME
      ==============================================
      1. INICIAR
      2. PONTUAÇÃO PARTICIPANTES
      3. REGRAS DO JOGO
      4. SAIR
      ==============================================
      INFORME SUA OPÇÃO: 
    */
}

// Henrique
fun iniciarJogo() {
    val (linhas, colunas) = capturarTamanhoTabuleiro()
    val tabuleiro = criarTabuleiro(linhas, colunas)
    val revelados = mutableSetOf<Pair<Int, Int>>()
    val paresEncontrados = mutableSetOf<String>()
    var ultimaEscolha: Pair<Int, Int>? = null

    while (paresEncontrados.size < (linhas * colunas) / 2) {
        exibirPontuacao();
        exibirTabuleiro(tabuleiro, revelados)
        print("Escolha uma linha: ")
        var linha = readln().toInt()
        linha -= 1
        print("Escolha uma coluna: ")
        var coluna = readln().toInt()
        coluna -= 1
    
        if (linha in 0 until linhas && coluna in 0 until colunas) {
            revelados.add(linha to coluna)
            exibirTabuleiro(tabuleiro, revelados)
    
            if (ultimaEscolha == null) {
                ultimaEscolha = linha to coluna
            } else {
                val (linhaAnterior, colunaAnterior) = ultimaEscolha
                val valorAnterior = tabuleiro[linhaAnterior][colunaAnterior].second
                val valorAtual = tabuleiro[linha][coluna].second
    
                if (valorAtual == valorAnterior) {
                    paresEncontrados.add(valorAtual)
                } else {
                    println("Pares errados!")
                    println("==============================================")
                    Thread.sleep(2000) // Aguarda 2 segundos antes de resetar
                    revelados.remove(ultimaEscolha)
                    revelados.remove(linha to coluna)
                    
                }
                ultimaEscolha = null
            }
        } else {
            println("Posição inválida!")
        }
    }
    println("Parabéns! Você encontrou todos os pares!")
}

// Arthur
fun capturarTamanhoTabuleiro(): Pair<Int, Int> {
    // Aqui você pode adicionar a lógica para capturar o tamanho do tabuleiro que o jogador deseja jogar.   
    // Retorne em Pair a quantidade de linhas e colunas do tabuleiro.
    // Se n souber, pesquise sobre Pair em Kotlin.
    // Exemplo de retorno: Pair(4, 4)
    return Pair(4, 4)
}

// Arthur
fun config() {
    // Aqui vc pode adicionar a lógica de configuração do jogo, como escolha de cores, nomes dos jogadores, etc.


     /*  Assim q salva as configurações dos jogadores no Map
     jogadores[nomeJogador1.ifBlank { "PARTICIPANTE01" }] = mapOf("cor" to cor1, "pontuacao" to 0)
     jogadores[nomeJogador2.ifBlank { "PARTICIPANTE02" }] = mapOf("cor" to cor2, "pontuacao" to 0)*/

}

// Laysa
fun exibirPontuacao() {
    // Aqui você pode adicionar a lógica para exibir a pontuação dos jogadores.
    // Utilize o map jogadores para acessar os dados dos jogadores.
}

// Matheus
fun exibirRegras() {
    // Aqui você vai dar um print nas regras do jogo. lembando que são 17 regras.
}

// Laysa
fun atualizarPontuacao(jogador: String, pontos: Int) {
    // Aqui você pode adicionar a lógica para atualizar a pontuação do jogador.
    // Utilize o map jogadores para acessar os dados dos jogadores e atualiza-los.
}

// Henrique
fun criarTabuleiro(linhas: Int, colunas: Int): Array<Array<Pair<String, String>>> {
    val totalCartas = (linhas * colunas) / 2
    val letras = ('A'..'Z').toList()
    val pares = (1..totalCartas).map { "${it}${letras.random()}" }.flatMap { listOf(it, it) }.shuffled()
    val coresAssociadas = atribuirCores(pares.distinct())

    val tabuleiro = Array(linhas) { Array(colunas) { Pair("", "") } }

    var index = 0
    for (i in 0 until linhas) {
        for (j in 0 until colunas) {
            val valor = pares[index]
            val cor = coresAssociadas[valor] ?: reset
            tabuleiro[i][j] = Pair(cor, valor)
            index++
        }
    }
    return tabuleiro
}

// Henrique
fun exibirTabuleiro(tabuleiro: Array<Array<Pair<String, String>>>, revelados: Set<Pair<Int, Int>>) {
    
    for (i in tabuleiro.indices) {
        if (i == 0){
            var num:String = "";
            for (j in 1..tabuleiro.size){
                num += "----${j}----";
            }
            print("\n---"); 
            println("${num}"); 
        }  
        print("${i+1}")

        for (j in tabuleiro[i].indices) {
            val (cor, valor) = tabuleiro[i][j]
            val simbolo = if (revelados.contains(i to j)) "$cor■■$valor■■$reset" else "■■??■■"
            print(" | $simbolo")
        }
        println(" |")
        println("--------".repeat((tabuleiro.size + 1)))
    }
}

// Arthur
fun atribuirCores(pares: List<String>): Map<String, String> {
    // Aqui você pode adicionar a lógica para atribuir cores as cartas do tabuleiro.
    // Retorne um Map com as cartas e suas respectivas cores.
    // Exemplo de retorno: mapOf("A1" to "\u001B[31m", "A2" to "\u001B[34m", "B1" to "\u001B[33m", "B2" to "\u001B[30m") 
    // A cor preta é obrigatória, as demais cores podem ser escolhidas aleatoriamente.
    // Utilize a lista cores para escolher as cores.
    // Exemplo de uso: cores[0] para pegar a cor vermelha.
    // Exemplo de uso: cores[1] para pegar a cor azul.
    // Exemplo de uso: cores[2] para pegar a cor amarela.
    // Exemplo de uso: cores[3] para pegar a cor preta.
    // Utilize a função shuffled() para embaralhar a lista de pares.
    // Exemplo de uso: cores.shuffled() para embaralhar a lista de cores.
    val coresMap = mutableMapOf<String, String>()
    
    
    return coresMap
}
