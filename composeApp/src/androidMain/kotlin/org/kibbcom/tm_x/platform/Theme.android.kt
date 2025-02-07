package org.kibbcom.tm_x.platform

import android.app.Activity
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
actual fun setStatusBarColor(
    color: Color,
    isDarkIcons: Boolean
) {
    val view = LocalView.current
    val activity = view.context as? Activity ?: return
    val window = activity.window

    window.statusBarColor = color.toArgb()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = isDarkIcons
    }
}