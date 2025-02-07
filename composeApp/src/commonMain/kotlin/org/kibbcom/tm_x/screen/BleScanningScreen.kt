package org.kibbcom.tm_x.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.delay
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.platform.ScanningViewModelFactory
import org.kibbcom.tm_x.Screen
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.models.BleDeviceCommon
import org.kibbcom.tm_x.platform.BackHandler
import org.kibbcom.tm_x.theme.CardBorderColor
import org.kibbcom.tm_x.viewmodel.ScanningViewModel


@Composable
fun BleScanningScreen(navigationState: NavigationNewState, paddingValues: PaddingValues, viewModel: ScanningViewModel = viewModel(factory = ScanningViewModelFactory())) {
    Column(
        modifier = Modifier
            .fillMaxSize() .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background), // Uses M3 background color
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BackHandler {
            navigationState.navigateBack()  // Handle back press
        }

        // Start scanning when the screen is composed
        LaunchedEffect(Unit) {
            viewModel.scanDevices()
        }

        val devicesNative by viewModel.devicesNative.collectAsState()
        val connectionState by viewModel.connectionState.collectAsState()

        LaunchedEffect(connectionState){
            println("Screen Device got connected")

            if (connectionState == BleConnectionStatus.CONNECTED ){
                println("Screen Device read method called connected")

                val serviceUuid ="EC7B0001-EDFF-4CCE-9CF8-3B175487D710"
               // val characteristicUuid = "EC7B0004-EDFF-4CCE-9CF8-3B175487D710"

                //Read and write Wifi Ssid (Read)
                 val WIFI_SSID = "EC7B0004-EDFF-4CCE-9CF8-3B175487D710"

                 val PASSWORD = "EC7B0005-EDFF-4CCE-9CF8-3B175487D710"

          //      viewModel.readBleData(serviceUuid,characteristicUuid)
                viewModel.writeBleData(serviceUuid,WIFI_SSID,"neeraj".toByteArray())
                delay(4000)
                viewModel.writeBleData(serviceUuid,PASSWORD,"vbvm8893".toByteArray())
            }
        }


        Text(
            text = when (connectionState) {
                BleConnectionStatus.IDLE -> "Idle"
                BleConnectionStatus.SCANNING -> "Scanning"
                BleConnectionStatus.BONDING -> "Bonding..."
                BleConnectionStatus.CONNECTING -> "Connecting..."
                BleConnectionStatus.CONNECTED -> "Connected"
                BleConnectionStatus.DISCONNECTED -> "Disconnected"
                else -> "Unknown State"
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(devicesNative) { device ->

                DeviceItem(device,navigationState)

            }
        }
    }
}




@Composable
fun DeviceItem(device: BleDeviceCommon, navigationState: NavigationNewState) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth() // Makes width full screen
            .wrapContentHeight() // Height adjusts based on content
            .clip(RoundedCornerShape(18.dp)).clickable {

                navigationState.navigateTo(Screen.DummyScreen)
//                        //todo for scanning
//                        viewModel.stopScanningDevice()
//                        viewModel.bondWithDevice(device.id)
            }
            .border(
                width = 1.dp,
                color = CardBorderColor,
                shape = RoundedCornerShape(18.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Light gray background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // No shadow
    )
    {
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
                        color = MaterialTheme.colorScheme.onSurface // Black text
                    )
                    Switch(
                        checked = false,
                        onCheckedChange = { /* Handle toggle */ },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.secondary, // Blue thumb
                            checkedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), // Light blue track
                            uncheckedThumbColor = Color.Gray, // Gray thumb when unchecked
                            uncheckedTrackColor = Color.LightGray // Light gray track when unchecked
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Device Name
                Text(
                    text = device.name?:"Unknown",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface // Black text
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
                            text = device.id,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Dark gray text (70% opacity)
                        )

                    }

                    // Connect Button
                    Button(
                        onClick = { /* Handle connect */ },
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary, // Blue button in M3
                            contentColor = Color.White // White text
                        ),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = if (false) "Connected" else "Connect",
                            color = if (false) Color(0xFF88D66C) else Color.White
                        )
                    }
                }
            }
        }
    }
}


