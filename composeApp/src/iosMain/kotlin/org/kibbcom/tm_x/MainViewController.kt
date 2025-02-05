package org.kibbcom.tm_x

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    val beaconDevices = mutableListOf<BeaconDevice>()
    val savedBeacons = mutableListOf<BeaconDevice>()
    App(
        navigationState = remember { NavigationState() }) }