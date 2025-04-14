package br.mangarosa.memorygame.activities.utils

import android.content.Context
import android.content.Intent

fun navigate(context: Context, destiny: Class<*>) {
    val intent = Intent(context, destiny)
    context.startActivity(intent)
}