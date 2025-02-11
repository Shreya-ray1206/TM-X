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
import org.kibbcom.tm_x.platform.PlatformUtils
import org.kibbcom.tm_x.viewmodel.PermissionsViewModel
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.scan_devices


@Composable
fun PermissionScreen(navigationState: NavigationNewState) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }
    val platformUtils = remember { PlatformUtils() } // ✅

    BindEffect(controller)

    val viewModel = viewModel {
        PermissionsViewModel(controller,platformUtils)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        val version = platformUtils.getAndroidVersion()
        val isAndroid = platformUtils.isAndroid()
        when {

            // ✅ If BLE permissions are granted & Bluetooth is ON
            (viewModel.bleScanPermissionState == PermissionState.Granted &&
                    viewModel.bleConnectPermissionState == PermissionState.Granted &&
                    viewModel.isBluetoothEnabled) -> {
                Text("BLE permissions granted!")
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = {
                    navigationState.navigateTo(Screen.BleScanning) // Navigate to next screen
                }) {
                    Text(stringResource(Res.string.scan_devices))
                }
            }

            // ✅ If on Android <12, also check Location permission & status
            (isAndroid && version < 31 &&
                    viewModel.bleScanPermissionState == PermissionState.Granted &&
                    viewModel.locationPermissionState == PermissionState.Granted &&
                    viewModel.isBluetoothEnabled &&
                    viewModel.isLocationEnabled) -> {
                Text("All required permissions granted!")
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = {
                    navigationState.navigateTo(Screen.BleScanning)
                }) {
                    Text(stringResource(Res.string.scan_devices))
                }
            }

            // ❌ If any permission is permanently denied
            (viewModel.bleScanPermissionState == PermissionState.DeniedAlways ||
                    viewModel.bleConnectPermissionState == PermissionState.DeniedAlways ||
                    viewModel.locationPermissionState == PermissionState.DeniedAlways) -> {
                Text("One or more permissions were permanently denied.")
                Spacer(modifier = Modifier.height(50.dp))

                Button(onClick = {
                    controller.openAppSettings() // Open settings to enable manually
                }) {
                    Text("Open app settings")
                }
            }

            // ❌ If Bluetooth is OFF
            !viewModel.isBluetoothEnabled -> {
                Text("Please turn on Bluetooth.")
            }

            // ❌ If on Android <12 and Location is OFF
            (isAndroid && version < 31 && !viewModel.isLocationEnabled) -> {
                Text("Please turn on Location for BLE scanning.")
            }

            // ❌ If permissions are not yet granted
            else -> {
                Text("Permissions are required for BLE scanning.")
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = { viewModel.provideOrRequestPermissions() }) {
                    Text("Request BLE permissions")
                }
            }
        }
    }



/*
    Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))


      *//*  when {

            viewModel.bleScanPermissionState == PermissionState.Granted &&
                    viewModel.bleConnectPermissionState == PermissionState.Granted -> {
                Text("BLE permissions granted!")
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = {
                    navigationState.navigateTo(Screen.BleScanning) // Navigate to next screen
                }) {
                    Text(stringResource(Res.string.scan_devices))
                }
            }

            // Handle case where either permission is denied permanently
            viewModel.bleScanPermissionState == PermissionState.DeniedAlways ||
                    viewModel.bleConnectPermissionState == PermissionState.DeniedAlways -> {
                Text("One or both BLE permissions were permanently declined.")
                Spacer(modifier = Modifier.height(50.dp))

                Button(onClick = {
                    controller.openAppSettings()
                }) {
                    Text("Open app settings")
                }
            }

            // Handle case where permissions are not granted or denied
            else -> {
                Spacer(modifier = Modifier.height(50.dp))
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = { viewModel.provideOrRequestBLEPermissions() }) {
                    Text("Request BLE permissions")
                }
            }
        }*//*

    }*/
}

@Composable
fun isAndroid(): Boolean = PlatformUtils::class.simpleName == "AndroidPlatformUtils"