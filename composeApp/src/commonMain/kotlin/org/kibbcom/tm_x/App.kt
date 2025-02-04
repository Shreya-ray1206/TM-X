package org.kibbcom.tm_x


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.lightColors
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.kibbcom.tm_x.screen.BleScanningScreen
import org.kibbcom.tm_x.viewmodel.PermissionsViewModel
import org.kibbcom.tm_x.theme.TmxAppTheme


private val LightColorPalette = lightColors(
    primary = Color.Black, // Black for TopBar and BottomBar
    primaryVariant = Color(0xFF333333), // Darker black for variations
    secondary = Color(0xFF03A9F4), // Light blue for secondary elements
    background = Color(0xFF353535), // Dark gray background
    surface = Color(0xFFF5F5F5).copy(alpha = 0.2f), // Transparent surface for cards
    onPrimary = Color.White, // White text on primary (black)
    onSecondary = Color.Black, // Black text on secondary
    onBackground = Color.White, // White text on background
    onSurface = Color.White, // White text on surface (cards)
)

val CardBorderColor = Color(0xFFCCCCCC)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = MaterialTheme.typography, // Use default typography
        shapes = MaterialTheme.shapes, // Use default shapes
        content = content
    )
}




@Composable
@Preview
fun App(navigationState: NavigationState = remember { NavigationState() }) {
    TmxAppTheme { // Apply the custom theme here
        MainScreen(navigationState)
    }
}


@Composable
fun MainScreen(navigationState: NavigationState) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }

    BindEffect(controller)

    val viewModel = viewModel {
        PermissionsViewModel(controller)
    }


    when (navigationState.currentScreen) {
        is Screen.Device -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            /*    when (viewModel.blePermissionState) {
                    PermissionState.Granted -> {
                        Text("BLE permission granted!")
                        Button(onClick = {
                            navigationState.navigateTo(Screen.BleScanning) // Navigate to next screen
                        }) {
                            Text("Scan Devices")
                        }
                    }

                    PermissionState.DeniedAlways -> {
                        Text("Permission was permanently declined.")
                        Button(onClick = { controller.openAppSettings() }) {
                            Text("Open app settings")
                        }
                    }

                    else -> {
                        Button(onClick = { viewModel.provideOrRequestBLEPermission() }) {
                            Text("Request permission")
                        }
                    }
                }*/


                when {
                    // Check if both Bluetooth permissions are granted
                    viewModel.bleScanPermissionState == PermissionState.Granted &&
                            viewModel.bleConnectPermissionState == PermissionState.Granted -> {
                        Text("BLE permissions granted!")
                        Button(onClick = {
                            navigationState.navigateTo(Screen.BleScanning) // Navigate to next screen
                        }) {
                            Text("Scan Devices")
                        }
                    }

                    // Handle case where either permission is denied permanently
                    viewModel.bleScanPermissionState == PermissionState.DeniedAlways ||
                            viewModel.bleConnectPermissionState == PermissionState.DeniedAlways -> {
                        Text("One or both BLE permissions were permanently declined.")
                        Button(onClick = { controller.openAppSettings() }) {
                            Text("Open app settings")
                        }
                    }

                    // Handle case where permissions are not granted or denied
                    else -> {
                        Button(onClick = { viewModel.provideOrRequestBLEPermissions() }) {
                            Text("Request BLE permissions")
                        }
                    }
                }

            }
        }

        is Screen.BleScanning -> {
            BleScanningScreen() // Show BLE scanning screen when navigated
        }

        Screen.Beacon -> {

        }
    }
}


@Composable
fun DeviceStatusIndicator(isConnected: Boolean) {
    val color = if (isConnected) Color(0xFF88D66C) else Color.Gray

    Icon(
        imageVector = Icons.Default.Check,
        contentDescription = "Connection Status", // Provide a meaningful description
        tint = color, // Apply the conditional color here
        modifier = Modifier.size(24.dp) // Set the size of the icon
    )
}

@Composable
fun Footer(navigationState: NavigationState){
    val items = listOf(Screen.Device, Screen.Beacon)
    val selectedItem = navigationState.currentScreen

    BottomNavigation(
        backgroundColor = Color(0xFF373737)
    ) {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    when (screen) {
                        is Screen.Device -> Icon(Icons.Default.Home, contentDescription = screen.toString())
                        is Screen.Beacon -> Icon(Icons.Default.LocationOn, contentDescription = screen.toString())
                        else -> {}
                    }
                },
                label = { Text(screen.toString()) },
                selected = selectedItem == screen,
                onClick = { navigationState.navigateTo(screen) },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = Color.White
                )
        }
    }

}

@Composable
fun Navigation(navigationState: NavigationState, modifier: Modifier = Modifier) {
    when (navigationState.currentScreen) {
        is Screen.Device -> DeviceScreen()
        is Screen.Beacon -> BeaconScreen()
        Screen.BleScanning -> {

        }
        else -> {}
    }
}
//
//@Composable
//fun DeviceScreen(
//
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//
//    ) {
//        // Add Header first
//        Header()
//
//        // Add Spacer if necessary to give space for the content
//        Spacer(modifier = Modifier.height(16.dp)) // Optional spacing
//
//        // Then add Content
//        Content()
//    }
//}

@Composable
fun Header() {
    val deviceName = "Connected Device Name"


    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Center the items horizontally
        verticalArrangement = Arrangement.Center // Center the items vertically
    ) {
        AsyncImage(
            model = "https://png.pngtree.com/background/20230527/original/pngtree-hd-headphone-on-black-background-with-pink-and-bright-blue-lights-picture-image_2760698.jpg",
            contentDescription = "im",
            modifier = Modifier
                .size(240.dp) // Size of the image
                .clip(CircleShape) // Makes the image circular
                .background(color = Color.Black)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = deviceName,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground
        )
    }
}

//@Composable
//fun Content() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
////            .weight(1f)
//            .padding(8.dp)
//    ) {
//        // Connected Device Details
////        ConnectedDeviceDetails()
//
//        // Grid of Devices
//        DeviceGrid()
//    }
//}


@Composable
fun ConnectedDeviceDetails() {

}



data class Device(
    val id: Int,
    val name: String,
    val infoLine1: String,
    val infoLine2: String,
    val isAuthenticated: Boolean,
    val isConnected: Boolean
)

@Composable
fun DeviceScreen() {
    val devices = listOf(
        Device(1, "JBL Flip 5", "E8:53:F3:51:94:97", "-82 dB", true, false),
        Device(2, "Sony WH-1000XM4", "A4:C3:F0:22:15:8E", "-75 dB", true, true),
        Device(3, "Apple AirPods Pro", "B2:45:67:89:10:AB", "-90 dB", false, false),
        Device(4, "Samsung Galaxy Buds Pro", "C3:D4:E5:F6:12:34", "-68 dB", true, false),
        Device(5, "Bose QuietComfort 35 II", "D5:E6:F7:18:29:3A", "-95 dB", false, true),
        Device(6, "Fitbit Versa 3", "E7:F8:19:20:3B:4C", "-60 dB", true, false),
        Device(7, "Logitech G533", "F9:10:2A:3B:4C:5D", "-88 dB", false, false),
        Device(8, "Xiaomi Mi Band 6", "1A:2B:3C:4D:5E:6F", "-72 dB", true, true),
        Device(9, "Beats Solo Pro", "2C:3D:4E:5F:6A:7B", "-80 dB", true, false),
        Device(10, "Garmin Venu 2", "3E:4F:5A:6B:7C:8D", "-85 dB", false, false)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 56.dp)
    ) {
        // Header as the first item
        item {
            Header()
        }

        items(
            count = devices.size,
            key = { index -> devices[index].id }
        ) { index ->
            DeviceItem(devices[index])
        }
    }
}

@Composable
fun DeviceItem(device: Device) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(150.dp) // Adjust height for row layout
            .width(200.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = 1.dp,
                color = CardBorderColor,
                shape = RoundedCornerShape(18.dp)
            ),
        backgroundColor = MaterialTheme.colors.surface, // Transparent light gray background
        elevation = 0.dp // Remove elevation to avoid shadow
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Left Column: Device Name, MAC Address, and Signal Strength
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // Authentication and Toggle in a Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Authentication",
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onSurface // Black text
                    )
                    Switch(
                        checked = device.isAuthenticated,
                        onCheckedChange = { /* Handle toggle */ },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colors.secondary, // Blue thumb
                            checkedTrackColor = MaterialTheme.colors.secondary.copy(alpha = 0.5f), // Light blue track
                            uncheckedThumbColor = Color.Gray, // Gray thumb when unchecked
                            uncheckedTrackColor = Color.LightGray // Light gray track when unchecked
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Device Name
                Text(
                    text = device.name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface // Black text
                )

                Spacer(modifier = Modifier.height(8.dp))

                // MAC Address, Signal Strength, and Connect Button in a Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // MAC Address and Signal Strength
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = device.infoLine1,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f) // Dark gray text (70% opacity)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Add spacing between MAC and Signal Strength
                        Text(
                            text = device.infoLine2,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f) // Dark gray text (70% opacity)
                        )
                    }

                    // Connect Button
                    Button(
                        onClick = { /* Handle connect */ },
                        enabled = !device.isConnected,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary, // Blue button
                            contentColor = Color.White // White text
                        ),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = if (device.isConnected) "Connected" else "Connect",
                            color = if (device.isConnected) Color(0xFF88D66C) else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BeaconScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Beacon Screen", fontSize = 24.sp)
    }
}