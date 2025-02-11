package org.kibbcom.tm_x.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.Screen
import org.kibbcom.tm_x.theme.primaryWhite
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.beacon_devices
import tm_x.composeapp.generated.resources.ble_devices
import tm_x.composeapp.generated.resources.logs
import tm_x.composeapp.generated.resources.settings

@Composable
fun getScreenTitle(screen: Screen): String {
    return when (screen) {
        is Screen.Device -> "TMX-Kibbcom" // First screen default title
        is Screen.Beacon -> stringResource(Res.string.beacon_devices)
        is Screen.BleScanning -> stringResource(Res.string.ble_devices)
        is Screen.Settings -> stringResource(Res.string.settings)
        is Screen.LogScreen -> stringResource(Res.string.logs)
        else -> "TMX-Kibbcom"
    }
}

@Composable
fun CustomTopBar(navigationState: NavigationNewState) {
    when (navigationState.currentScreen) {
        is Screen.BleScanning -> DefaultTopBar(getScreenTitle(navigationState.currentScreen))
        is Screen.Beacon -> DefaultTopBar(getScreenTitle(navigationState.currentScreen))
        is Screen.Settings -> DefaultTopBar(getScreenTitle(navigationState.currentScreen))
        is Screen.LogScreen -> DefaultTopBar(getScreenTitle(navigationState.currentScreen))
        is Screen.DummyScreen -> BackTopBar(navigationState,"New Screen") // Special TopBar with a back button
        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(title: String) {
    TopAppBar(
        title = { Text(title, color = primaryWhite) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(navigationState: NavigationNewState,title: String) {
    TopAppBar(
        title = { Text(title, color = primaryWhite) },
        navigationIcon = {
            IconButton(onClick = {navigationState.navigateBack()}) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}
