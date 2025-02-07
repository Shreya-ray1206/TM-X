package org.kibbcom.tm_x.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import org.kibbcom.tm_x.platform.setStatusBarColor

@Composable
fun TmxAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = darkPrimaryGrey,
            secondary = secondaryGrey,
            background = darkBackground,
            surface = darkSurface
        )
    } else {
        lightColorScheme(
            primary = lightPrimaryBlue,  // Green top bar
            secondary = lightDarkBlue,
            background = lightAppBackGround,
            surface = lightSurface


        )
    }

    // Set Status Bar color for Android
    setStatusBarColor(
        color = if (darkTheme) black else lightDarkBlue,
        isDarkIcons = !darkTheme
    )

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
