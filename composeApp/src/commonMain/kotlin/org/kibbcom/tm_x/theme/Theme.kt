package org.kibbcom.tm_x.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun TmxAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Auto-detect dark mode
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = primaryGrey,
            secondary = secondaryGrey,
            background = primaryDarkGray
        )
    } else {
        lightColorScheme(
            primary = primaryGrey,
            secondary = secondaryGrey,
            background = primaryLightGray
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,  // Custom typography
        content = content
    )
}