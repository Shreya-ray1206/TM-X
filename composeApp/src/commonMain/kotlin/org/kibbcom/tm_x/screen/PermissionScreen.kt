package org.kibbcom.tm_x.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.jetbrains.compose.resources.stringResource
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.Screen
import org.kibbcom.tm_x.viewmodel.PermissionsViewModel
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.scan_devices


@Composable
fun PermissionScreen(navigationState: NavigationNewState) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }

    BindEffect(controller)

    val viewModel = viewModel {
        PermissionsViewModel(controller)
    }


    Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))


        when {
            // Check if both Bluetooth permissions are granted
            viewModel.bleScanPermissionState == PermissionState.Granted &&
                    viewModel.bleConnectPermissionState == PermissionState.Granted -> {
                Text("BLE permissions granted!")
                Button(onClick = {

                    navigationState.navigateTo(Screen.BleScanning) // Navigate to next screen
                }) {
                    Spacer(modifier = Modifier.height(50.dp))
                    Text(stringResource(Res.string.scan_devices))
                }
            }

            // Handle case where either permission is denied permanently
            viewModel.bleScanPermissionState == PermissionState.DeniedAlways ||
                    viewModel.bleConnectPermissionState == PermissionState.DeniedAlways -> {
                Text("One or both BLE permissions were permanently declined.")
                Button(onClick = {

                    controller.openAppSettings()

                }) {
                    Text("Open app settings")
                }
            }

            // Handle case where permissions are not granted or denied
            else -> {
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = { viewModel.provideOrRequestBLEPermissions() }) {
                    Text("Request BLE permissions")
                }
            }
        }

    }
}