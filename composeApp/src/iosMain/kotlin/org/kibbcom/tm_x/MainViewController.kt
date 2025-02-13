package org.kibbcom.tm_x

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import appDatabase.DBFactory

fun MainViewController() = ComposeUIViewController {
    val db = DBFactory().createDatabase()
    App(
    db = db,
    navigationState = remember { NavigationNewState() }) }