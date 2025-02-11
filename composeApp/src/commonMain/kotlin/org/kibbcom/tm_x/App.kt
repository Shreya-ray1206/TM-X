package org.kibbcom.tm_x


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.kibbcom.tm_x.screen.BeaconScreen
import org.kibbcom.tm_x.screen.BleScanningScreen
import org.kibbcom.tm_x.screen.CustomTopBar
import org.kibbcom.tm_x.screen.DummyScreen
import org.kibbcom.tm_x.screen.PermissionScreen
import org.kibbcom.tm_x.screen.SettingsScreen
import org.kibbcom.tm_x.theme.TmxAppTheme
import org.kibbcom.tm_x.theme.lightPrimaryBlue
import org.kibbcom.tm_x.theme.primaryWhite
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.beacon_devices
import tm_x.composeapp.generated.resources.ble_devices
import tm_x.composeapp.generated.resources.logs
import tm_x.composeapp.generated.resources.settings

@Composable
@Preview
fun App(navigationState: NavigationNewState = remember { NavigationNewState() }) {
    TmxAppTheme { // Apply the custom theme here
        Scaffold(
            topBar = { CustomTopBar(navigationState) },
            bottomBar = {
                if (navigationState.showBottomBar) {
                    BottomNavigationBar(navigationState)
                }
            }
        ) { paddingValues ->  // Capture padding from Scaffold
            when (navigationState.currentScreen) {
                is Screen.BleScanning -> BleScanningScreen(navigationState, paddingValues)
                is Screen.Beacon -> BeaconScreen(navigationState, paddingValues)
                is Screen.Settings -> SettingsScreen(navigationState, paddingValues)
                is Screen.LogScreen -> LogScreen(navigationState, paddingValues)
                is Screen.Permission -> PermissionScreen(navigationState)
                is Screen.DummyScreen -> DummyScreen(navigationState,paddingValues)
                else -> {}
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navigationState: NavigationNewState) {
    val items = listOf(Screen.BleScanning, Screen.Beacon, Screen.Settings,Screen.LogScreen)
    val isDarkTheme = isSystemInDarkTheme() // Detect dark mode

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface, // Background color
        contentColor = MaterialTheme.colorScheme.onSurface // Ensures contrast
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            is Screen.BleScanning -> Icons.Outlined.LocationOn
                            is Screen.Beacon -> Icons.Outlined.LocationOn
                            is Screen.Settings -> Icons.Outlined.Settings
                            is Screen.LogScreen -> Icons.Outlined.Info
                            else -> Icons.Outlined.Settings
                        },
                        contentDescription = screen.toString()
                    )
                },
                label = {
                    Text(
                        getNameTitle(screen),
                        color = if (navigationState.currentScreen == screen) {
                            if (isDarkTheme) primaryWhite else lightPrimaryBlue
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                },
                selected = navigationState.currentScreen == screen,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryWhite, // Keep white icon for selected items
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant, // Gray for unselected
                    indicatorColor = MaterialTheme.colorScheme.primary // Background for selected item
                ),
                onClick = { navigationState.navigateTo(screen) }
            )
        }
    }
}

@Composable
fun getNameTitle(screen: Screen): String {
    return when (screen) {

        is Screen.Beacon -> stringResource(Res.string.beacon_devices)
        is Screen.BleScanning -> stringResource(Res.string.ble_devices)
        is Screen.Settings -> stringResource(Res.string.settings)
        is Screen.LogScreen -> stringResource(Res.string.logs)
        else -> "TMX-Kibbcom"
    }
}




