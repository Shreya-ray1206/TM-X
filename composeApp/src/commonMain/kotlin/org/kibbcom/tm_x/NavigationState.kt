package org.kibbcom.tm_x

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class NavigationState {

    var currentScreen : Screen by mutableStateOf(Screen.Device)
        private set

    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }
}

sealed class Screen {
    object Device : Screen(){
        override fun toString(): String = "Device"
    }
    object Beacon : Screen(){
        override fun toString(): String = "Beacon"
    }
    object Settings : Screen() {
        override fun toString(): String = "Settings"
    }
}