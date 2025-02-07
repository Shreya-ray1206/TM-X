package org.kibbcom.tm_x


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.kibbcom.tm_x.screen.BeaconScreen
import org.kibbcom.tm_x.screen.BleScanningScreen
import org.kibbcom.tm_x.screen.PermissionScreen
import org.kibbcom.tm_x.screen.SettingsScreen
import org.kibbcom.tm_x.theme.TmxAppTheme
import org.kibbcom.tm_x.theme.green
import org.kibbcom.tm_x.theme.primaryWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(navigationState: NavigationNewState = remember { NavigationNewState() }) {
    if (navigationState.showBottomBar) {
        TmxAppTheme { // Apply the custom theme here

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = getScreenTitle(navigationState.currentScreen),
                                color = primaryWhite
                            )
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        windowInsets = TopAppBarDefaults.windowInsets
                    )
                },
                bottomBar = { BottomNavigationBar(navigationState) }
            ) { paddingValues ->  // Capture padding from Scaffold
                when (navigationState.currentScreen) {
                    is Screen.BleScanning -> BleScanningScreen(navigationState, paddingValues)
                    is Screen.Beacon -> BeaconScreen(navigationState, paddingValues)
                    is Screen.Settings -> SettingsScreen(navigationState, paddingValues)
                    else -> {}
                }
            }



        }
    }else{
        PermissionScreen(navigationState)

    }



}


@Composable
fun BottomNavigationBar(navigationState: NavigationNewState) {
    val items = listOf(Screen.BleScanning, Screen.Beacon, Screen.Settings)
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
                            else -> Icons.Outlined.Settings
                        },
                        contentDescription = screen.toString()
                    )
                },
                label = {
                    Text(
                        screen.toString(),
                        color = if (navigationState.currentScreen == screen) {
                            if (isDarkTheme) primaryWhite else green
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
fun getScreenTitle(screen: Screen): String {
    return when (screen) {
        is Screen.Device -> "TMX-Kibbcom" // First screen default title
        is Screen.Beacon -> "Beacon Devices"
        is Screen.BleScanning -> "Scanning Devices"
        is Screen.Settings -> "Settings"
        else -> "TMX-Kibbcom"
    }
}



