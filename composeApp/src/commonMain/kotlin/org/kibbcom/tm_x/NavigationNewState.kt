package org.kibbcom.tm_x

import androidx.compose.runtime.mutableStateListOf


class NavigationNewState {

    private val _screenStack = mutableStateListOf<Screen>(Screen.Permission)
    val currentScreen: Screen get() = _screenStack.last()

    val showBottomBar: Boolean
        get() = currentScreen in listOf(Screen.BleScanning, Screen.Beacon, Screen.Settings,Screen.LogScreen)

    fun navigateTo(screen: Screen) {
        if (screen in listOf(Screen.BleScanning)) {
            // Replace the top screen instead of adding to stack
            if (_screenStack.isNotEmpty()) {
                _screenStack[_screenStack.size - 1] = screen
            } else {
                _screenStack.add(screen)
            }
        } else {
            // For other screens, add to the stack normally
            _screenStack.add(screen)
        }
    }

    fun clearStack() {
        _screenStack.clear()
    }

    fun navigateBack() {
        if (_screenStack.size > 1) {
            _screenStack.removeAt(_screenStack.size - 1) // Safe alternative
        } else {

            //todo
            println("We need to exit from here ")

            // If only one screen is left, exit the app
            //exitApplication() // Custom function to close app
        }
    }
}



