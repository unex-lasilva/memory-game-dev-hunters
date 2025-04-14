package br.mangarosa.memorygame.activities.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import br.mangarosa.memorygame.R

@Composable
fun BlueCircledSquare(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.blue_square),
        contentDescription = "Um quadrado azul arredondado",
        modifier = modifier
    )
}

@Composable
fun GreenNext(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.green_next),
        contentDescription = "Um ícone verde indicando próxima ação",
        modifier = modifier
    )
}

@Composable
fun Home(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.blue_home),
        contentDescription = "Um ícone para voltar à tela inicial",
        modifier = modifier
    )
}

@Composable
fun RedBack(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.red_back),
        contentDescription = "Um ícone vermelho indicando retrocedência",
        modifier = modifier
    )
}

@Composable
fun MainLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "A logo principal do jogo da memória, contendo o texto \"Memory Game\"",
        modifier = modifier
    )
}

@Composable
fun Wallpaper(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.wallpaper),
        contentDescription = "Papel de parede contendo desenhos de montanha rodeadas por várias nuvens",
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}