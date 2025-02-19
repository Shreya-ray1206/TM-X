package org.kibbcom.tm_x.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.kibbcom.tm_x.theme.darkPrimaryGrey
import org.kibbcom.tm_x.theme.lightPrimaryBlue

@Composable
fun getCommonCardColor(): Color {
    return  if (!isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.surfaceVariant // This will now use the theme value

}

@Composable
fun getToolbarAdditionColor(): Color {

    return if (isSystemInDarkTheme()) darkPrimaryGrey else lightPrimaryBlue
}