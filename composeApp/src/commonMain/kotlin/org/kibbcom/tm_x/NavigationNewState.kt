package org.kibbcom.tm_x

import androidx.compose.runtime.mutableStateListOf

class NavigationNewState {

    private val _screenStack = mutableStateListOf<Screen>(Screen.Device)
    val currentScreen: Screen get() = _screenStack.last()

    fun navigateTo(screen: Screen) {
        _screenStack.add(screen)
    }

    fun navigateBack() {

        if (_screenStack.size > 1) {
            _screenStack.removeAt(_screenStack.size - 1) // Safe alternative
        }
    }


}

