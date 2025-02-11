package org.kibbcom.tm_x


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.lightColors
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import okio.FileSystem


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

    AppTheme { // Apply the custom theme here
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                // App title
                                Text(
                                    text = "Kibbcom TM-X",
                                    color = MaterialTheme.colors.onPrimary
                                )

                                //todo code improvement
                                Spacer(modifier = Modifier.width(150.dp))

                                //todo code improvement  replace with drawable image

                                AsyncImage(
                                    model = "https://cdn-icons-png.flaticon.com/512/5464/5464177.png",
                                    contentDescription = "Connection icon",
                                    modifier = Modifier
                                        .size(25.dp) // Small size for the icon

                                )
                            }
                        }
                    },
                    backgroundColor = Color(0xFF373737), // Black
                    actions = {
                        DeviceStatusIndicator(isConnected = true)
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor =  Color(0xFF373737),
                    contentColor = MaterialTheme.colors.onPrimary
                ) {

                    Footer(navigationState)
                }
            }
        ) {
                innerPadding ->
            Navigation(
                navigationState,
                Modifier.padding(innerPadding)
            )
        }
    }
}



@Composable
fun DeviceStatusIndicator(isConnected: Boolean) {
    val color = if (isConnected) Color(0xFF88D66C) else Color.Gray


//    val iconPainter = painterResource(resource = R.drawable.app_icon)

     //Display the custom icon
//    Image(
//        painter = iconPainter,
//        contentDescription = "Connection Status",
//        modifier = Modifier.size(24.dp),
//        colorFilter = ColorFilter.tint(color)
//    )
}

@Composable
fun Footer(navigationState: NavigationState){
    val items = listOf(Screen.Device, Screen.Beacon, Screen.Settings, Screen.Log)
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
                        is Screen.Settings -> Icon(Icons.Default.Settings, contentDescription = screen.toString())
                        is Screen.Log -> Icon(Icons.Default.Info, contentDescription = screen.toString()) // Add icon for Log
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
        is Screen.Settings -> SettingsScreen()
        is Screen.Log -> LogScreen()

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
            .wrapContentSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = "https://png.pngtree.com/background/20230527/original/pngtree-hd-headphone-on-black-background-with-pink-and-bright-blue-lights-picture-image_2760698.jpg",
            contentDescription = "im",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
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
        Device(1, "JBL Flip 5", "E8:53:F3:51:94:97", "-82 dB", true, true),
        Device(2, "Sony WH-1000XM4", "A4:C3:F0:22:15:8E", "-75 dB", true, false),
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




data class BeaconDevice(
    val name: String,
    val macAddress: String,
    val rssi: String,
    val major: Int,
    val minor: Int,
    val isSaved :Boolean
)

@Composable
fun BeaconItem(beacon: BeaconDevice) {
    var isSaved by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface,
//        ),
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = beacon.name,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mac Address : ${beacon.macAddress}",
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Rssi : ${ beacon.rssi}",
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row() {
                Text(
                    text = "Major : ${beacon.major} Minor : ${beacon.minor}",
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(70.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(

                        onClick = {
                            isSaved = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF88D66C)
                        ),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(text = "Save", color = Color.White)
                    }
                }
            }
        }
    }
}

