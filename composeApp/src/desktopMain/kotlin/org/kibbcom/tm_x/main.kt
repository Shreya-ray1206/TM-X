package org.kibbcom.tm_x

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import appDatabase.DBFactory

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TMX",
    ) {
        val db = DBFactory().createDatabase()
        App(db = db, navigationState = remember { NavigationNewState() })
    }
}