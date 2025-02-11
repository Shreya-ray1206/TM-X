package org.kibbcom.tm_x.viewmodel

/*import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import kotlinx.coroutines.launch
import org.kibbcom.tm_x.platform.PlatformUtils

class PermissionsViewModel(
    private val controller: PermissionsController,
    private val platformUtils: PlatformUtils
) : ViewModel() {

    // Mutable state for Bluetooth Scan and Connect permissions
    var bleScanPermissionState by mutableStateOf(PermissionState.NotDetermined)
    var bleConnectPermissionState by mutableStateOf(PermissionState.NotDetermined)

    init {
        viewModelScope.launch {
            // Initialize both Bluetooth permissions states
            bleScanPermissionState = controller.getPermissionState(Permission.BLUETOOTH_SCAN)
            bleConnectPermissionState = controller.getPermissionState(Permission.BLUETOOTH_CONNECT)
        }
    }

    // Function to request or provide Bluetooth permissions
    fun provideOrRequestBLEPermissions() {
        viewModelScope.launch {
            try {
                // Request Bluetooth Scan permission
                controller.providePermission(Permission.BLUETOOTH_SCAN)
                bleScanPermissionState = PermissionState.Granted

                // Request Bluetooth Connect permission
                controller.providePermission(Permission.BLUETOOTH_CONNECT)
                bleConnectPermissionState = PermissionState.Granted
            } catch (e: DeniedAlwaysException) {
                // Handle DeniedAlwaysException for both permissions
                if (e.permission == Permission.BLUETOOTH_SCAN) {
                    bleScanPermissionState = PermissionState.DeniedAlways
                } else if (e.permission == Permission.BLUETOOTH_CONNECT) {
                    bleConnectPermissionState = PermissionState.DeniedAlways
                }
            } catch (e: DeniedException) {
                // Handle DeniedException for both permissions
                if (e.permission == Permission.BLUETOOTH_SCAN) {
                    bleScanPermissionState = PermissionState.Denied
                } else if (e.permission == Permission.BLUETOOTH_CONNECT) {
                    bleConnectPermissionState = PermissionState.Denied
                }
            } catch (e: RequestCanceledException) {
                e.printStackTrace()
            }
        }
    }
}*/

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.*
import kotlinx.coroutines.launch
import org.kibbcom.tm_x.platform.PlatformUtils

class PermissionsViewModel(
    private val controller: PermissionsController,
    private val platformUtils: PlatformUtils
) : ViewModel() {

    // Permission states
    var bleScanPermissionState by mutableStateOf(PermissionState.NotDetermined)
    var bleConnectPermissionState by mutableStateOf(PermissionState.NotDetermined)
    var locationPermissionState by mutableStateOf(PermissionState.NotDetermined)

    // Bluetooth & Location Status
    var isBluetoothEnabled by mutableStateOf(false)
    var isLocationEnabled by mutableStateOf(false)
    val androidVersion = platformUtils.getAndroidVersion()
    val isAndroid = platformUtils.isAndroid()
    init {
        viewModelScope.launch {
            if (isAndroid) {


                if (androidVersion >= 31) { // API 31+ (Android 12+)
                    bleScanPermissionState = controller.getPermissionState(Permission.BLUETOOTH_SCAN)
                    bleConnectPermissionState = controller.getPermissionState(Permission.BLUETOOTH_CONNECT)
                } else { // Below API 31 (Android <12)
                    bleScanPermissionState = controller.getPermissionState(Permission.BLUETOOTH_LE)
                    locationPermissionState = controller.getPermissionState(Permission.LOCATION)
                }

                isLocationEnabled = platformUtils.isLocationEnabled()
            } else {
                // iOS (Only Bluetooth permission is required)
                bleScanPermissionState = controller.getPermissionState(Permission.BLUETOOTH_LE)
            }

            isBluetoothEnabled = platformUtils.isBluetoothEnabled()
        }
    }

    fun provideOrRequestPermissions() {
        viewModelScope.launch {
            try {
                if (isAndroid) {


                    if (androidVersion >= 31) { // Android 12+
                        controller.providePermission(Permission.BLUETOOTH_SCAN)
                        controller.providePermission(Permission.BLUETOOTH_CONNECT)
                        bleScanPermissionState = PermissionState.Granted
                        bleConnectPermissionState = PermissionState.Granted
                    } else { // Android <12
                        controller.providePermission(Permission.BLUETOOTH_LE)
                        controller.providePermission(Permission.LOCATION)
                        bleScanPermissionState = PermissionState.Granted
                        locationPermissionState = PermissionState.Granted
                    }
                } else {
                    // iOS
                    controller.providePermission(Permission.BLUETOOTH_LE)
                    bleScanPermissionState = PermissionState.Granted
                }
            } catch (e: DeniedAlwaysException) {
                handlePermissionDeniedAlways(e.permission)
            } catch (e: DeniedException) {
                handlePermissionDenied(e.permission)
            } catch (e: RequestCanceledException) {
                e.printStackTrace()
            }
        }
    }

    private fun handlePermissionDeniedAlways(permission: Permission) {
        when (permission) {
            Permission.BLUETOOTH_LE -> bleScanPermissionState = PermissionState.DeniedAlways
            Permission.BLUETOOTH_SCAN -> bleScanPermissionState = PermissionState.DeniedAlways
            Permission.BLUETOOTH_CONNECT -> bleConnectPermissionState = PermissionState.DeniedAlways
            Permission.LOCATION -> locationPermissionState = PermissionState.DeniedAlways
            Permission.CAMERA -> TODO()
            Permission.GALLERY -> TODO()
            Permission.STORAGE -> TODO()
            Permission.WRITE_STORAGE -> TODO()
            Permission.COARSE_LOCATION -> TODO()
            Permission.BACKGROUND_LOCATION -> TODO()
            Permission.REMOTE_NOTIFICATION -> TODO()
            Permission.RECORD_AUDIO -> TODO()
            Permission.BLUETOOTH_ADVERTISE -> TODO()
            Permission.CONTACTS -> TODO()
            Permission.MOTION -> TODO()
        }
    }

    private fun handlePermissionDenied(permission: Permission) {
        when (permission) {
            Permission.BLUETOOTH_LE -> bleScanPermissionState = PermissionState.Denied
            Permission.BLUETOOTH_SCAN -> bleScanPermissionState = PermissionState.Denied
            Permission.BLUETOOTH_CONNECT -> bleConnectPermissionState = PermissionState.Denied
            Permission.LOCATION -> locationPermissionState = PermissionState.Denied
            Permission.CAMERA -> TODO()
            Permission.GALLERY -> TODO()
            Permission.STORAGE -> TODO()
            Permission.WRITE_STORAGE -> TODO()
            Permission.COARSE_LOCATION -> TODO()
            Permission.BACKGROUND_LOCATION -> TODO()
            Permission.REMOTE_NOTIFICATION -> TODO()
            Permission.RECORD_AUDIO -> TODO()
            Permission.BLUETOOTH_ADVERTISE -> TODO()
            Permission.CONTACTS -> TODO()
            Permission.MOTION -> TODO()
        }
    }
}


