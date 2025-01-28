package org.kibbcom.tm_x


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.lightColors
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory


private val LightColorPalette = lightColors(
    primary = Color.Black, // Black for TopBar and BottomBar
    primaryVariant = Color(0xFF333333), // Darker black for variations
    secondary = Color(0xFF03A9F4), // Light blue for secondary elements
    background = Color.Black, // Dark gray background
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

        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        val viewModel = viewModel {
            PermissionsViewModel(controller)
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(viewModel.state) {
                PermissionState.Granted -> {
                    Text("Record audio permission granted!")
                }
                PermissionState.DeniedAlways -> {
                    Text("Permission was permanently declined.")
                    Button(onClick = {
                        controller.openAppSettings()
                    }) {
                        Text("Open app settings")
                    }
                }
                else -> {
                    Button(
                        onClick = {
                            viewModel.provideOrRequestRecordAudioPermission()
                        }
                    ) {
                        Text("Request permission")
                    }
                }
            }
        }

        /*Scaffold(

            topBar = {
                TopAppBar(
                    title = {
                        Text("Kibbcom TM-X", color = MaterialTheme.colors.onPrimary)
                    },
                    backgroundColor = MaterialTheme.colors.primary, // Black
                    actions = {
                        DeviceStatusIndicator(isConnected = true)
                    }
                )
            },
            bottomBar = { Footer(navigationState) }
        ) { innerPadding ->
            Navigation(navigationState, Modifier.padding(innerPadding))
        }*/
    }
}



@Composable
fun DeviceStatusIndicator(isConnected: Boolean) {
    val color = if (isConnected) Color(0xFF88D66C) else Color.Gray

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(shape = CircleShape)
            .background(color)
    ) {}
}

@Composable
fun Footer(navigationState: NavigationState){
    val items = listOf(Screen.Device, Screen.Beacon)
    val selectedItem = navigationState.currentScreen

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary
    ) {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    when (screen) {
                        is Screen.Device -> Icon(Icons.Default.Home, contentDescription = screen.toString())
                        is Screen.Beacon -> Icon(Icons.Default.LocationOn, contentDescription = screen.toString())
                    }
                },
                label = { Text(screen.toString()) },
                selected = selectedItem == screen,
                onClick = { navigationState.navigateTo(screen) }
            )
        }
    }

}

@Composable
fun Navigation(navigationState: NavigationState, modifier: Modifier = Modifier) {
    when (navigationState.currentScreen) {
        is Screen.Device -> DeviceScreen()
        is Screen.Beacon -> BeaconScreen()
    }
}

@Composable
fun DeviceScreen(

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        // Add Header first
        Header()

        // Add Spacer if necessary to give space for the content
        Spacer(modifier = Modifier.height(16.dp)) // Optional spacing

        // Then add Content
        Content()
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

@Composable
fun Header() {
    val deviceName = "Connected Device Name"


    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Center the items horizontally
        verticalArrangement = Arrangement.Center // Center the items vertically
    ) {
        AsyncImage(
            model = "https://img.freepik.com/free-vector/isometric-data-visualization-concept-background_23-2148106145.jpg",
            contentDescription = "im",
            modifier = Modifier
                .size(250.dp) // Size of the image
                .clip(CircleShape) // Makes the image circular
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = deviceName,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
fun Content() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .weight(1f)
            .padding(16.dp)
    ) {
        // Connected Device Details
//        ConnectedDeviceDetails()

        // Grid of Devices
        DeviceGrid()
    }
}


@Composable
fun ConnectedDeviceDetails() {

}



data class Device(
    val id: String,
    val name: String,
    val infoLine1: String,
    val infoLine2: String,
    val isAuthenticated: Boolean,
    val isConnected: Boolean
)

@Composable
fun DeviceGrid() {
    val devices = listOf(
        Device(
            id = "1",
            name = "Device 1",
            infoLine1 = "Info Line 1",
            infoLine2 = "Info Line 2",
            isAuthenticated = true,
            isConnected = false
        ),
        Device(
            id = "2",
            name = "Device 2",
            infoLine1 = "Info Line 1",
            infoLine2 = "Info Line 2",
            isAuthenticated = true,
            isConnected = false
        ),
        Device(
            id = "3",
            name = "Device 3",
            infoLine1 = "Info Line 1",
            infoLine2 = "Info Line 2",
            isAuthenticated = true,
            isConnected = false
        ),
        Device(
            id = "4",
            name = "Shreya Tm-x 4",
            infoLine1 = "Info Line 1",
            infoLine2 = "Info Line 2",
            isAuthenticated = true,
            isConnected = true
        ),
        Device(
            id = "5",
            name = "Device 5",
            infoLine1 = "Info Line 1",
            infoLine2 = "Info Line 2",
            isAuthenticated = true,
            isConnected = false
        ),
        Device(
            id = "6",
            name = "Device 6",
            infoLine1 = "Info Line 1",
            infoLine2 = "Info Line 2",
            isAuthenticated = true,
            isConnected = false
        ),
        Device(
            id = "7",
            name = "Device 5",
            infoLine1 = "Info Line 1",
            infoLine2 = "Info Line 2",
            isAuthenticated = true,
            isConnected = false
        ),
        Device(
            id = "8",
            name = "Device 6",
            infoLine1 = "Info Line 1",
            infoLine2 = "Info Line 2",
            isAuthenticated = true,
            isConnected = false
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(devices) { device ->
            DeviceItem(device)
        }
    }
}

@Composable
fun DeviceItem(device: Device) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = CardBorderColor,
                shape = MaterialTheme.shapes.medium
            ),
        backgroundColor = MaterialTheme.colors.surface, // Transparent light gray background
        elevation = 0.dp // Remove elevation to avoid shadow
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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

            Text(
                text = device.name,
                fontSize = 20.sp,
                color = MaterialTheme.colors.onSurface // Black text
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = device.infoLine1,
                fontSize = 17.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f) // Dark gray text (70% opacity)
            )
            Text(
                text = device.infoLine2,
                fontSize = 17.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f) // Dark gray text (70% opacity)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = { /* Handle connect */ },
                    enabled = !device.isConnected,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary, // Blue button
                        contentColor = Color.White // White text
                    )
                ) {
                    Text(
                        text = if (device.isConnected) "Connected" else "Connect",
                        color = if (device.isConnected) Color(0xFF88D66C) else Color.White )

                }
            }
        }
    }
}

