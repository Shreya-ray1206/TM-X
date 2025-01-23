package org.kibbcom.tm_x

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun imageResource(name: String): Any {
    val context = LocalContext.current
    return ContextCompat.getDrawable(context, context.resources.getIdentifier(name, "drawable", context.packageName))!!
}