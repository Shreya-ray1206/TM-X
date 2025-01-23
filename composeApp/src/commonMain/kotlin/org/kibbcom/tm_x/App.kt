package org.kibbcom.tm_x


import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp





@Composable
@Preview
fun App(navigationState: NavigationState = remember { NavigationState() } ) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Kibbcom TM-X")
                },
                actions = {
                    DeviceStatusIndicator(isConnected = true)
                }
            )
        },
        bottomBar = { Footer(navigationState) }
    ){
            innerPadding ->
        Navigation(navigationState, Modifier.padding(innerPadding))
    }

}

@Composable
fun DeviceStatusIndicator(isConnected: Boolean){
    val color = if (isConnected) Color.Green else Color.Gray

    Box (
        modifier = Modifier
            .size(24.dp)
            .clip(shape = CircleShape)
            .background(color)

    ){  }
}

@Composable
fun Footer(navigationState: NavigationState){
    val items = listOf(Screen.Device, Screen.Beacon)
    val selectedItem = navigationState.currentScreen

    BottomNavigation {
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
fun DeviceScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header()
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
fun Header(){
    val deviceName = "Connected Device Name"
    val deviceImage = imageResource("device_img")

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically
        horizontalArrangement = Arrangement.Center
    ){
//        Image(
//            painter = painterResource(resource = Res.drawable(deviceImage)),
//            contentDescription = "Device Image",
//            modifier = Modifier.size(48.dp)
//        )
        Spacer(modifier = Modifier.width(8.dp)) // Using dp here
        Text(
            text = deviceName,
            style = MaterialTheme.typography.h6
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
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Row for Authentication + Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Authentication",
                    fontSize = 12.sp
                )
                Switch(
                    checked = device.isAuthenticated,
                    onCheckedChange = { /* Handle toggle */ }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Device Name
            Text(
                text = device.name,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = device.infoLine1,
                fontSize = 17.sp
            )
            Text(
                text = device.infoLine2,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))


            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = { /* Handle connect */ },
                    enabled = !device.isConnected
                ) {
                    Text(text = if (device.isConnected) "Connected" else "Connect")
                }
            }
        }
    }
}

