package br.mangarosa.memorygame.activities.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.mangarosa.memorygame.activities.MainActivity
import br.mangarosa.memorygame.activities.utils.navigate

@Composable
fun GameLogo() {
    MainLogo(modifier = Modifier
        .fillMaxWidth(0.9f)
        .fillMaxSize(0.3f)
    )
}

@Composable
fun ScreenBackground() {
    Wallpaper(modifier = Modifier.fillMaxSize())
}