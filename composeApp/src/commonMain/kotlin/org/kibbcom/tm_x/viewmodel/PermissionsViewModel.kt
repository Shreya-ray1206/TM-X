package org.kibbcom.tm_x.viewmodel

import androidx.compose.runtime.getValue
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


class PermissionsViewModel(
    private val controller: PermissionsController
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
}

