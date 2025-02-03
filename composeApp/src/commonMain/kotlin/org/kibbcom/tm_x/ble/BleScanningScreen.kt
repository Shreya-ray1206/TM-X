package org.kibbcom.tm_x.ble

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juul.kable.State
import org.kibbcom.tm_x.theme.primaryGrey

@Composable
fun BleScanningScreen( viewModel: ScanningViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        // Start scanning when the screen is composed
        LaunchedEffect(Unit) {
            viewModel.startScanning()
        }

        val connectionState by viewModel.connectionStatus.collectAsState()



        Spacer(modifier = Modifier.height(16.dp))
        val devices = viewModel.devices.collectAsState(emptyList())

        Text(
            text = when (connectionState) {
                is State.Connected -> "Connected ✅"
                is State.Connecting -> "Connecting... ⏳"
                is State.Disconnected -> "Disconnected ❌"
                is State.Disconnecting -> "Disconnecting... 🔄"
                else -> "Unknown State"
            }
        )

        Spacer(modifier = Modifier.height(16.dp))



        // Vertical scrollable list of cards
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items
        ) {
            items(devices.value) { device -> // Iterate over devices list
                // Card for each device
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            // Handle click event here
                             viewModel.connectToDevice(device.advertisement)
                        },
                    elevation = CardDefaults.cardElevation(4.dp), // Set elevation
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Device: ${device.advertisement?.name}",  // Device name
                            style = MaterialTheme.typography.body1,
                            color = Color.White
                        )

                        Text(
                            text = "Tx Power: ${device.advertisement?.rssi}",  // Tx Power
                            style = MaterialTheme.typography.body2,
                                    color = Color.White
                        )
                    }
                }
            }
        }


    }
}


