package br.mangarosa.memorygame.activities.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

const val DEFAULT_LOGO_SIZE = 340

@Composable
fun GameLogo(modifier: Modifier = Modifier, logoSize: Int = DEFAULT_LOGO_SIZE) {
    MainLogo(modifier = modifier.size(logoSize.dp))
}

@Composable
fun HomeIcon() {
    Home(modifier = Modifier.fillMaxWidth(0.5f))
}

@Composable
fun ScreenBackground() {
    Wallpaper(modifier = Modifier.fillMaxSize())
}