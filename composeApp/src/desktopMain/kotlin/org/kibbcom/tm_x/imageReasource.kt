package org.kibbcom.tm_x

import androidx.compose.runtime.Composable
import javax.imageio.ImageIO

@Composable
actual fun imageResource(name: String): Any {
    // Load the image from the classpath
    val imageStream = object {}.javaClass.getResourceAsStream("/$name.png")
        ?: throw IllegalArgumentException("Image not found: /$name.png")
    return ImageIO.read(imageStream)
}