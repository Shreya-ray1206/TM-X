package org.kibbcom.tm_x

import androidx.compose.runtime.mutableStateListOf

class NavigationNewState {

    private val _screenStack = mutableStateListOf<Screen>(Screen.Permission)
    val currentScreen: Screen get() = _screenStack.last()

    val showBottomBar: Boolean
        get() = currentScreen in listOf(Screen.BleScanning, Screen.Beacon, Screen.Settings)


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

