package org.kibbcom.tm_x

import androidx.compose.runtime.Composable
import platform.UIKit.UIImage

@Composable
actual fun imageResource(name: String): Any {
    return UIImage.imageNamed(name)!!
}