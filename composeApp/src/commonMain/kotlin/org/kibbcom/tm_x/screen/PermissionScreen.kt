package org.kibbcom.tm_x.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.common.getCommonCardColor
import org.kibbcom.tm_x.common.getToolbarAdditionColor
import org.kibbcom.tm_x.db.AppDatabase
import org.kibbcom.tm_x.platform.PlatformUtils
import org.kibbcom.tm_x.platform.ScanningViewModelFactory
import org.kibbcom.tm_x.viewmodel.PermissionsViewModel
import org.kibbcom.tm_x.viewmodel.ScanningViewModel
import tm_x.composeapp.generated.resources.Res
import tm_x.composeapp.generated.resources.all_granted
import tm_x.composeapp.generated.resources.beacon
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
fun PermissionScreen(appDatabase: AppDatabase,navigationState: NavigationNewState, paddingValues: PaddingValues,scanningViewModel: ScanningViewModel = viewModel(factory = ScanningViewModelFactory(db = appDatabase))) {
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



    PermissionUI(viewModel, isAndroid, version, controller, paddingValues,navigationState,platformUtils,scanningViewModel)




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
    navigationState: NavigationNewState,
    platformUtils: PlatformUtils,
    scanningViewModel: ScanningViewModel

) {
    val appVersion = platformUtils.getAppVersion()
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val lastConnectedDevice by scanningViewModel.lastConnectedDevice.collectAsState()
        val connectionState by scanningViewModel.connectionState.collectAsState()
        // Top Box with Rounded Bottom Corners
        Column(
                 modifier = getCommonModifierForAdditionToolbar(100.dp)
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
            /*val ble_scan = Color(0xFFafcaff)
            val beacon_scan = Color(0xFFdaf0fb)
            val last_connected = Color(0xFFf9fee0)*/

            val ble_scan = Color(0xFFafcaff)
            val all_saved_beacon = Color(0xFFfef4e0)
            val beacon_scan = Color(0xFFdaf0fb)
            val last_connected = Color(0xFFf4fdc7)



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardItem(stringResource(Res.string.scan_ble), resource = Res.drawable.bluetooth, moreText = "Scan nearby ble devices", modifier = Modifier.weight(1f), colorForIcon = ble_scan) {
                    navigationState.navigateTo(Screen.BleScanning)
                }

                CardItem(stringResource(Res.string.scan_beacon), resource = Res.drawable.beacon, moreText = "Scan nearby beacons", modifier = Modifier.weight(1f), colorForIcon = beacon_scan) {
                    navigationState.navigateTo(Screen.Beacon)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardItem(stringResource(Res.string.saved_beacon), resource = Res.drawable.beacon, moreText = "All the saved beacons.", modifier = Modifier.weight(1f), colorForIcon = all_saved_beacon) {
                    navigationState.navigateTo(Screen.SavedBeacon)
                }

                var moreText = "N/A"
                val defaultTitle = stringResource(Res.string.last_connected) // Get the string first
                var cardTitle by remember { mutableStateOf(defaultTitle) }


                if (lastConnectedDevice != null) {

                    moreText = lastConnectedDevice?.name.toString()
                }

                LaunchedEffect(connectionState){
                    if (connectionState == BleConnectionStatus.CONNECTED) {
                        println("BLE Connection in UI cardtitle")

                        cardTitle = "Connected"

                    }
                }

                CardItem(cardTitle, resource = Res.drawable.bluetooth, moreText = moreText, modifier = Modifier.weight(1f), colorForIcon = last_connected) {
                 //   lastConnectedDevice?.let { it1 -> scanningViewModel.bondWithDevice(it1) }
                    navigationState.navigateTo(Screen.DeviceDetailScreen)
                }


            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Gray.copy(alpha = 0.3f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = stringResource(Res.string.user_manual),
                        style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline),
                        modifier = Modifier.clickable { println("User Manual clicked!") }
                    )

                    Text(
                        text = "TM-X Info",
                        style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline),
                        modifier = Modifier.clickable {
                            val rawUrl = "www.trety.com/tm-4"
                            val formattedUrl = formatUrl(rawUrl)

                            platformUtils.openUrl(formattedUrl)
                            println("Team Info clicked!")

                        }
                    )
                }

                val versionTitle = stringResource(Res.string.version_info)
                Text(
                    text = "$versionTitle: $appVersion",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Copyright ©2025 Kibbcom India Pvt Ltd.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }


    }
}

fun formatUrl(url: String): String {
    val trimmedUrl = url.trim()
    return if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
        "https://$trimmedUrl" // Prefer HTTPS for security
    } else {
        trimmedUrl
    }
}

@Composable
fun CardItem(title: String,
             moreText : String? = null,
             resource: DrawableResource,
             modifier: Modifier,
             colorForIcon: Color, onClick: () -> Unit) {




    Card(
        modifier = modifier.height(120.dp) .clickable { onClick() },
        shape = RoundedCornerShape(8.dp), // Keep all corners rounded at 25.dp
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = getCommonCardColor())
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Title in Center
            val darkerTextColor = colorForIcon.copy(red = colorForIcon.red * 0.9f, green = colorForIcon.green * 0.9f, blue = colorForIcon.blue * 0.9f)

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
                    color = darkerTextColor //Color(0xFF7a8db2)
                )
            }


            // Circular Overlay at Top-Right Corner
            Box(
                modifier = Modifier
                    .size(60.dp) // Size of the circle
                    .offset(x = 10.dp, y = -10.dp) // Position it slightly outside the card
                    .clip(CircleShape) // Keep the white overlay circular
                    .background(colorForIcon)
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






