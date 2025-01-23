package org.kibbcom.tm_x

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { App( navigationState = remember { NavigationState() }) }