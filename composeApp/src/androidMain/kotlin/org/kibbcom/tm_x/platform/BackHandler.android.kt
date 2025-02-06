package org.kibbcom.tm_x.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*

@Composable
actual fun BackHandler(onBack: () -> Unit) {
    val callback = rememberUpdatedState(onBack)
    BackHandler(enabled = true) { callback.value() }  // ✅ Prevent infinite recomposition
}