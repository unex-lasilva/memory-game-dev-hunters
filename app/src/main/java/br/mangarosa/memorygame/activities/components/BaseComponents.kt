package br.mangarosa.memorygame.activities.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import br.mangarosa.memorygame.R

object CircledSquare {

    @Composable
    fun Blue(modifier: Modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.blue_square),
            contentDescription = "Um quadrado azul arredondado",
            modifier = modifier
        )
    }

    @Composable
    fun Red(modifier: Modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.red_square),
            contentDescription = "Um quadrado vermelho arredondado",
            modifier = modifier
        )
    }

}

object Close {

    @Composable
    fun Red(modifier: Modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.red_x_button),
            contentDescription = "Um ícone vermelho com um X no meio indicando saída da operação atual",
            modifier = modifier
        )
    }

}

object FieldBase {

    @Composable
    fun Green(modifier: Modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.green_field),
            contentDescription = "Uma base de cor verde com borda branca para compor um campo de texto",
            modifier = modifier
        )
    }

    @Composable
    fun Red(modifier: Modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.red_field),
            contentDescription = "Uma base de cor vermelha com borda branca para compor um campo de texto",
            modifier = modifier
        )
    }
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