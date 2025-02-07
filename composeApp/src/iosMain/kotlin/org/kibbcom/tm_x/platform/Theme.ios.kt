package org.kibbcom.tm_x.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIWindowScene

@Composable
actual fun setStatusBarColor(
    color: Color,
    isDarkIcons: Boolean
) {
   /* val uiColor = UIColor(
        red = color.red.toDouble(),
        green = color.green.toDouble(),
        blue = color.blue.toDouble(),
        alpha = color.alpha.toDouble()
    )

    val scene = UIApplication.sharedApplication.connectedScenes.firstOrNull() as? UIWindowScene
    val statusBarStyle = if (isDarkIcons) UIStatusBarStyleLightContent else UIStatusBarStyleDarkContent

    scene?.windows?.firstOrNull()?.let { window ->
        window.overrideUserInterfaceStyle = if (isDarkIcons) UIUserInterfaceStyle.UIUserInterfaceStyleLight else UIUserInterfaceStyle.UIUserInterfaceStyleDark
        window.rootViewController?.setNeedsStatusBarAppearanceUpdate()
    }

    // Fallback for older iOS versions
    if (Build.VERSION.SDK_INT < 13) {
        UIApplication.sharedApplication.statusBarStyle = statusBarStyle
    }*/
}