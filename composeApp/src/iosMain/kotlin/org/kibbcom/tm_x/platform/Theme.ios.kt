package org.kibbcom.tm_x.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.Foundation.performSelectorOnMainThread
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController
import platform.UIKit.setStatusBarStyle

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun setStatusBarColor(
    color: Color,
    isDarkIcons: Boolean
) {
    val viewController = getRootViewController()

    // Run on the main thread
    viewController?.performSelectorOnMainThread(
        NSSelectorFromString("setNeedsStatusBarAppearanceUpdate"),
        null,
        false
    )

    // Change status bar style based on `isDarkIcons`
    val statusBarStyle = if (isDarkIcons) {
        UIStatusBarStyleDarkContent // Light background, dark icons (iOS 13+)
    } else {
        UIStatusBarStyleLightContent // Dark background, light icons
    }

    // Apply the new style
    UIApplication.sharedApplication.setStatusBarStyle(statusBarStyle)
}

fun getRootViewController(): UIViewController? {
    return UIApplication.sharedApplication.keyWindow?.rootViewController
}

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
