package org.kibbcom.tm_x.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.kibbcom.tm_x.platform.setStatusBarColor

@Composable
fun TmxAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = primaryGrey,
            secondary = secondaryGrey,
            background = primaryDarkGray,
            surface = card
        )
    } else {
        lightColorScheme(
            primary = green,  // Green top bar
            secondary = secondaryGrey,
            background = primaryLightGray,

        )
    }

    // Set Status Bar color for Android
    setStatusBarColor(
        color = if (darkTheme) black else darkGreen,
        isDarkIcons = !darkTheme
    )

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
