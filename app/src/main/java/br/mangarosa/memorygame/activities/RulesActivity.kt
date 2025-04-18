package br.mangarosa.memorygame.activities

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme
import br.mangarosa.memorygame.R

import br.mangarosa.memorygame.activities.components.GameLogo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import br.mangarosa.memorygame.activities.components.ScreenBackground
class RulesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryGameTheme {
                Surface (modifier = Modifier.fillMaxSize()) {


                }
            }
        }
    }
}

@Preview
@Composable
fun TelaDeRegras() {

    Box(modifier = Modifier.fillMaxSize()) {
        ScreenBackground()
        Box (
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()){



            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f)
                    .fillMaxSize(0.8f)
                    .clip(RoundedCornerShape(7.dp))
                    .background(Color.White.copy(alpha = 0.6f))


            ) {Text(
                text = "REGRAS", // O texto que você quer exibir
                modifier = Modifier
                    .padding(top = 60.dp)
                    .align(Alignment.TopCenter)
                    .width(100.dp),

                style = TextStyle(
                    fontSize = 26.sp, // Tamanho da fonte
                    fontWeight = FontWeight.Bold, // Peso da fonte
                    color = Color.Black // Cor do texto
                )
            )
                Text(
                    text = "1. Cada participante deve ter atribuído a si uma cor (vermelho ou azul) no início do jogo.", // O texto que você quer exibir
                    modifier = Modifier
                        .padding(bottom = 400.dp)
                        .align(Alignment.Center)
                        .background(color = Color.Cyan)
                        .width(260.dp),

                    style = TextStyle(
                        fontSize = 11.sp, // Tamanho da fonte
                        fontWeight = FontWeight.Bold, // Peso da fonte
                        color = Color.Black // Cor do texto
                    )
                )
                Text(
                    text = "2. Todo participante deve ter um nome registrado. Senão, o nome padrão “PARTICIPANTE01” e “PARTICIPANTE02” devem ser atribuídos a cada um das(os) participantes.", // O texto que você quer exibir
                    modifier = Modifier
                        .padding(bottom = 310.dp)
                        .align(Alignment.Center)
                        .background(color = Color.Cyan)
                        .width(260.dp),

                    style = TextStyle(
                        fontSize = 11.sp, // Tamanho da fonte
                        fontWeight = FontWeight.Bold, // Peso da fonte
                        color = Color.Black // Cor do texto
                    )
                )
                Text(
                    text = "3.  Cada participante possui uma pontuação atrelada a si.", // O texto que você quer exibir
                    modifier = Modifier
                        .padding(bottom = 220.dp)
                        .align(Alignment.Center)
                        .background(color = Color.Cyan)
                        .width(260.dp),

                    style = TextStyle(
                        fontSize = 11.sp, // Tamanho da fonte
                        fontWeight = FontWeight.Bold, // Peso da fonte
                        color = Color.Black // Cor do texto
                    )
                )
                Text(
                    text = "4. Se a(o) participante encontrar um par de cartas com fundo amarelo, fatura 1 ponto.", // O texto que você quer exibir
                    modifier = Modifier
                        .padding(bottom = 160.dp)
                        .align(Alignment.Center)
                        .background(color = Color.Cyan)
                        .width(260.dp),

                    style = TextStyle(
                        fontSize = 11.sp, // Tamanho da fonte
                        fontWeight = FontWeight.Bold, // Peso da fonte
                        color = Color.Black // Cor do texto
                    )
                )
                Text(
                    text = "5. Se a(o) participante encontrar um par de cartas com o fundo da sua cor, fatura 5 pontos.", // O texto que você quer exibir
                    modifier = Modifier
                        .padding(bottom = 100.dp)
                        .align(Alignment.Center)
                        .background(color = Color.Cyan)
                        .width(260.dp),

                    style = TextStyle(
                        fontSize = 11.sp, // Tamanho da fonte
                        fontWeight = FontWeight.Bold, // Peso da fonte
                        color = Color.Black // Cor do texto
                    )
                )
                Text(
                    text = "6. Se a(o) participante encontrar um par de cartas com o fundo da cor de seu adversário e errar, perde 2 pontos. Porém, se acertar, ganha apenas 1 ponto.", // O texto que você quer exibir
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .align(Alignment.Center)
                        .background(color = Color.Cyan)
                        .width(260.dp),

                    style = TextStyle(
                        fontSize = 11.sp, // Tamanho da fonte
                        fontWeight = FontWeight.Bold, // Peso da fonte
                        color = Color.Black // Cor do texto
                    )
                )
                Text(
                    text = "7. RA(o) participante não pode ter pontuação negativa. Se ela(ele) perder mais pontos do que possui, ficará com a pontuação zerada7.", // O texto que você quer exibir
                    modifier = Modifier
                        .padding(top = 85.dp)
                        .align(Alignment.Center)
                        .background(color = Color.Cyan)
                        .width(260.dp),

                    style = TextStyle(
                        fontSize = 11.sp, // Tamanho da fonte
                        fontWeight = FontWeight.Bold, // Peso da fonte
                        color = Color.Black // Cor do texto
                    )
                )
                Text(
                    text = "8.Se a(o) participante encontrar uma carta com o fundo preto e errar o seu par,perde o jogo, mesmo que tenha a pontuação superior à da(o) outra(o) participante. Mas se acertar, ganha o jogo.", // O texto que você quer exibir
                    modifier = Modifier
                        .padding(top = 200.dp)
                        .align(Alignment.Center)
                        .background(color = Color.Cyan)
                        .width(260.dp),

                    style = TextStyle(
                        fontSize = 11.sp, // Tamanho da fonte
                        fontWeight = FontWeight.Bold, // Peso da fonte
                        color = Color.Black // Cor do texto
                    )
                )

            }

        }
        Box( modifier = Modifier.padding(bottom = 5.dp).fillMaxSize(), contentAlignment = Alignment.TopCenter ){
            GameLogo()
        }
    }

}