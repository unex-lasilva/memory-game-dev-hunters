package br.mangarosa.memorygame.activities.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun MainText(text: String, fontSize: Int, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Color.White,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        modifier = modifier,
        style = TextStyle(shadow = Shadow(color = Color.Black, offset = Offset(6f, 6f), blurRadius = 12f))
    )
}