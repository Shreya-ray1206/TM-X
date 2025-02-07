package org.kibbcom.tm_x

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NavigationNewState {

    private val _screenStack = mutableStateListOf<Screen>(Screen.Device)
    val currentScreen: Screen get() = _screenStack.last()
    var showBottomBar: Boolean by mutableStateOf(false) // Initially hidden

    fun navigateTo(screen: Screen) {
        _screenStack.add(screen)
    }

    fun clearStack(){
        _screenStack.clear()
    }

    fun navigateBack() {

        if (_screenStack.size > 1) {
            _screenStack.removeAt(_screenStack.size - 1) // Safe alternative
        }
    }


}

