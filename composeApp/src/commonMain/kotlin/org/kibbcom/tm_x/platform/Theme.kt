package org.kibbcom.tm_x.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun setStatusBarColor(color: Color, isDarkIcons: Boolean)