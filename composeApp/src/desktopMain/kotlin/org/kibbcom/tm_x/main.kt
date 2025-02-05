package org.kibbcom.tm_x

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "HelloWorld",
    ) {
        App(navigationState = remember { NavigationState() })
    }
}