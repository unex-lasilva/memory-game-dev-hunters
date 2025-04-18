package br.mangarosa.memorygame.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.mangarosa.memorygame.R
import br.mangarosa.memorygame.activities.components.GameLogo
import br.mangarosa.memorygame.activities.components.ScreenBackground
import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme

class ScoresActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoryGameTheme {
                ScoresScreen(onVoltar = { finish() })
            }
        }
    }
}

@Composable
fun ScoresScreen(onVoltar: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Fundo
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
                Surface (modifier = Modifier.fillMaxSize()) {

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(220.dp)
                    .height(120.dp)
                    .padding(bottom = 32.dp)
            )

            // Título
            androidx.compose.material3.Text(
                text = "Pontuação Final",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Pontuações
            androidx.compose.material3.Text(
                text = "Jogador 1: 12 pontos",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            androidx.compose.material3.Text(
                text = "Jogador 2: 9 pontos",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Botão Voltar com clique
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable { onVoltar() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.button), // Corrigido aqui
                    contentDescription = "Botão Voltar",
                    modifier = Modifier
                        .width(220.dp)
                        .height(60.dp)
                )
                androidx.compose.material3.Text(
                    text = "Voltar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
}