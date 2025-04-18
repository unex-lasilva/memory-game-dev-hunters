//package br.mangarosa.memorygame.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp

//import br.mangarosa.memorygame.activities.components.GreenNext
//import br.mangarosa.memorygame.activities.components.Home
//import br.mangarosa.memorygame.activities.components.MainText
//import br.mangarosa.memorygame.activities.components.RedBack
//import br.mangarosa.memorygame.activities.components.ScreenBackground
//import br.mangarosa.memorygame.activities.ui.theme.MemoryGameTheme
//import br.mangarosa.memorygame.activities.utils.navigate
//import br.mangarosa.memorygame.classes.CardBoard
//import br.mangarosa.memorygame.classes.Player

//
//@Composable
//private fun PlayerNameField(playerNumber: Int, playerName: String, playerColor: String, onNameChange: (String) -> Unit) {
//    val indicatorColor = Color(0xFF478CCA)
//    val containerColor = Color(0xFFAFD7F6)
//    val labelColor = Color.DarkGray
//    val textColor = Color.Black
//    val defaultAlpha = 0.4f
//    TextField(
//        value = playerName,
//        onValueChange = { onNameChange(it) },
//        label = { Text("Participante $playerNumber") },
//        leadingIcon = { ColorIndicator(playerColor) },
//        singleLine = true,
//        colors = TextFieldDefaults.colors(
//            unfocusedContainerColor = containerColor.copy(alpha = defaultAlpha),
//            unfocusedIndicatorColor = indicatorColor,
//            unfocusedPlaceholderColor = Color(0xFF777777),
//            unfocusedLabelColor = labelColor,
//            unfocusedTextColor = textColor,
//            focusedContainerColor = containerColor.copy(alpha = defaultAlpha * 1.5f),
//            focusedIndicatorColor = indicatorColor,
//            focusedPlaceholderColor = Color(0xFF2A2A2A),
//            focusedLabelColor = labelColor,
//            focusedTextColor = textColor,
//        ),
//        modifier = Modifier
//            .padding(bottom = 15.dp)
//            .height(70.dp)
//    )
//}
