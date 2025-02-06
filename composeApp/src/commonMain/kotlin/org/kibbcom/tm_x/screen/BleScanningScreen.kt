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
import androidx.compose.material.Button
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
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.delay
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.ScanningViewModelFactory
import org.kibbcom.tm_x.Screen
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.platform.BackHandler
import org.kibbcom.tm_x.viewmodel.ScanningViewModel


@Composable
fun BleScanningScreen(  navigationState: NavigationNewState,viewModel: ScanningViewModel = viewModel(factory = ScanningViewModelFactory())) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
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

                        navigationState.navigateTo(Screen.DummyScreen)

//                        //todo for scanning
//                        viewModel.stopScanningDevice()
//                        viewModel.bondWithDevice(device.id)
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


