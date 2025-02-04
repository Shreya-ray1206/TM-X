package org.kibbcom.tm_x.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kibbcom.tm_x.ScanningViewModelFactory
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.viewmodel.ScanningViewModel

@Composable
fun BleScanningScreen(viewModel: ScanningViewModel = viewModel(factory = ScanningViewModelFactory())) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val devicesNative by viewModel.devicesNative.collectAsState()
        val connectionState by viewModel.connectionState.collectAsState()

        // Start scanning when the screen is composed
        LaunchedEffect(Unit) {
            viewModel.scanDevices()
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).clickable {
                        viewModel.stopScanningDevice()
                        viewModel.bondWithDevice(device.id)
                    },
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Name: ${device.name}", color = Color.White)
                        Text(text = "ID: ${device.id}", color = Color.White)
                    }
                }
            }
        }
    }
}


