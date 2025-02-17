package org.kibbcom.tm_x.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.kibbcom.tm_x.NavigationNewState
import org.kibbcom.tm_x.Screen
import org.kibbcom.tm_x.platform.PlatformUtils
import org.kibbcom.tm_x.theme.getToolbarAdditionColor
import org.kibbcom.tm_x.theme.primaryWhite
import org.kibbcom.tm_x.viewmodel.PermissionsViewModel
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.all_granted
import tm_x.composeapp.generated.resources.beacon
import tm_x.composeapp.generated.resources.ble_devices
import tm_x.composeapp.generated.resources.bluetooth
import tm_x.composeapp.generated.resources.last_connected
import tm_x.composeapp.generated.resources.permission_messages
import tm_x.composeapp.generated.resources.permission_required
import tm_x.composeapp.generated.resources.saved_beacon
import tm_x.composeapp.generated.resources.scan_beacon
import tm_x.composeapp.generated.resources.scan_ble
import tm_x.composeapp.generated.resources.some_permission_missing
import tm_x.composeapp.generated.resources.turn_on_ble
import tm_x.composeapp.generated.resources.turn_on_loc
import tm_x.composeapp.generated.resources.user_manual
import tm_x.composeapp.generated.resources.version_info


@Composable
fun PermissionScreen(navigationState: NavigationNewState, paddingValues: PaddingValues) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }
    val platformUtils = remember { PlatformUtils() } // ✅

    BindEffect(controller)

    val viewModel = viewModel {
        PermissionsViewModel(controller, platformUtils)
    }

    val version = platformUtils.getAndroidVersion()
    val isAndroid = platformUtils.isAndroid()

    PermissionUI(viewModel, isAndroid, version, controller, paddingValues,navigationState)


    /*

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            val version = platformUtils.getAndroidVersion()
            val isAndroid = platformUtils.isAndroid()
            if (isAndroid){

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
            }else{
                Text("BLE permissions granted!")
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = {
                    navigationState.navigateTo(Screen.BleScanning) // Navigate to next screen
                }) {
                    Text(stringResource(Res.string.scan_devices))
                }
            }

        }
    */


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
fun PermissionUI(
    viewModel: PermissionsViewModel, // Pass your ViewModel instance
    isAndroid: Boolean,
    version: Int,
    controller: PermissionsController,
    paddingValues: PaddingValues,
    navigationState: NavigationNewState

) {
    val appVersion = "1.0.0"
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        // Top Box with Rounded Bottom Corners
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                    ambientColor = Color.White,
                    spotColor = Color.White
                )
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(getToolbarAdditionColor()) // Change to darkPrimaryGrey if needed
        ) {

            if(isAndroid){
                when {
                    // ✅ BLE Permissions Granted & Bluetooth is ON
                    viewModel.bleScanPermissionState == PermissionState.Granted &&
                            viewModel.bleConnectPermissionState == PermissionState.Granted &&
                            viewModel.isBluetoothEnabled -> {
                        PermissionRow(
                            message = stringResource(Res.string.all_granted)
                        )
                    }

                    // ✅ On Android <12, also check Location permission & status
                    isAndroid && version < 31 &&
                            viewModel.bleScanPermissionState == PermissionState.Granted &&
                            viewModel.locationPermissionState == PermissionState.Granted &&
                            viewModel.isBluetoothEnabled &&
                            viewModel.isLocationEnabled -> {
                        PermissionRow(
                            message = stringResource(Res.string.all_granted),

                        )
                    }

                    // ❌ Any permission permanently denied
                    viewModel.bleScanPermissionState == PermissionState.DeniedAlways ||
                            viewModel.bleConnectPermissionState == PermissionState.DeniedAlways ||
                            viewModel.locationPermissionState == PermissionState.DeniedAlways -> {
                        PermissionRow(
                            message =stringResource(Res.string.some_permission_missing),
                            buttonText = "Open App Settings"
                        ) {
                            controller.openAppSettings()
                        }
                    }

                    // ❌ Bluetooth is OFF
                    !viewModel.isBluetoothEnabled -> {
                        PermissionRow(
                            message = stringResource(Res.string.turn_on_ble),
                            buttonText = "Enable Bluetooth"
                        ) {
                            // viewModel.enableBluetooth() // Call method to enable Bluetooth
                        }
                    }

                    // ❌ On Android <12 and Location is OFF
                    isAndroid && version < 31 && !viewModel.isLocationEnabled -> {
                        PermissionRow(
                            message = stringResource(Res.string.turn_on_loc),

                            buttonText = "Enable Location"
                        ) {
                            // viewModel.enableLocation() // Call method to enable Location
                        }
                    }

                    // ❌ Permissions are not yet granted
                    else -> {
                        PermissionRow(
                            message = stringResource(Res.string.permission_required),

                            buttonText = "Request BLE Permissions"
                        ) {
                            viewModel.provideOrRequestPermissions()
                        }
                    }
                }
            }else{
                PermissionRow(
                    message = stringResource(Res.string.permission_messages)
                )
            }


        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val ble_scan = Color(0xFFafcaff)
            val beacon_scan = Color(0xFFdaf0fb)
            val last_connected = Color(0xFFf9fee0)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardItem(stringResource(Res.string.scan_ble), resource = Res.drawable.bluetooth, moreText = "Scan nearby ble devices", modifier = Modifier.weight(1f), color = ble_scan) {
                    navigationState.navigateTo(Screen.BleScanning)
                }

                CardItem(stringResource(Res.string.scan_beacon), resource = Res.drawable.beacon, moreText = "Scan nearby beacons", modifier = Modifier.weight(1f), color = beacon_scan) {
                    navigationState.navigateTo(Screen.Beacon)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardItem(stringResource(Res.string.saved_beacon), resource = Res.drawable.beacon, moreText = "All the saved beacons.", modifier = Modifier.weight(1f), color = beacon_scan) {
                    navigationState.navigateTo(Screen.SavedBeacon)
                }

                CardItem(stringResource(Res.string.last_connected), resource = Res.drawable.bluetooth, moreText = "Ctek Njord 1.2.6", modifier = Modifier.weight(1f), color = last_connected) {
                    println("Last Connected clicked!")
                }


            }
        }


        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter) // Apply alignment here inside Box
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(Res.string.user_manual),
                   style = MaterialTheme.typography.headlineSmall.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable { println("User Manual clicked!") }
                )

                Text(
                    text = "TM-X Info",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable { println("Team Info clicked!") }
                )
                Spacer(Modifier.height(2.dp))
                val versionTitle  = stringResource(Res.string.version_info)
                Text(
                    text = "$versionTitle :-$appVersion",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(Modifier.height(5.dp))

                Text(
                    text = "Copyright ©2025 Kibbcom India Pvt Ltd.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }


    }
}

@Composable
fun CardItem(title: String,
             moreText : String? = null,
             resource: DrawableResource,
             modifier: Modifier,
             color: Color,onClick: () -> Unit) {
    Card(
        modifier = modifier.height(120.dp) .clickable { onClick() },
        shape = RoundedCornerShape(8.dp), // Keep all corners rounded at 25.dp
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Title in Center

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                // "More text" positioned above the main title
                moreText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall
                         // Add some space between the texts
                    )
                }


                // Title at the bottom-left
                Text(
                    text = title,
                    color = color
                )
            }


            // Circular Overlay at Top-Right Corner
            Box(
                modifier = Modifier
                    .size(60.dp) // Size of the circle
                    .offset(x = 10.dp, y = -10.dp) // Position it slightly outside the card
                    .clip(CircleShape) // Keep the white overlay circular
                    .background(color)
                    .align(Alignment.TopEnd), // Position at top-right
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(resource), // Replace with your image
                    contentDescription = "Card Icon",
                    modifier = Modifier.size(40.dp).padding(8.dp) // Image inside the circle
                )
            }
        }
    }
}


@Composable
fun PermissionRow(message: String, buttonText: String? = null, onClick: (() -> Unit)? = null) {
    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {

        Text(text = message, color = Color.White, fontSize = 16.sp)

        // Show button only if buttonText and onClick are provided
        if (!buttonText.isNullOrEmpty() && onClick != null) {
            Button(onClick = onClick) {
                Text(text = buttonText, color = Color.White)
            }
        }
    }
}






