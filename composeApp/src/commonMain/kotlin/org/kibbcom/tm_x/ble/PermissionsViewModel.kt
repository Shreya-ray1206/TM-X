package org.kibbcom.tm_x.ble

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juul.kable.Scanner
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import kotlinx.coroutines.launch
class PermissionsViewModel (
    private val controller: PermissionsController
): ViewModel() {

     var state by mutableStateOf(PermissionState.NotDetermined)
        private set

    private val scanner = Scanner()

 /*   private val _devices = MutableStateFlow<List<String>>(emptyList()) // You can store the device names here
    val devices: StateFlow<List<String>> = _devices
*/
    init {
        viewModelScope.launch {
            state = controller.getPermissionState(Permission.BLUETOOTH_SCAN)
        }
    }

    fun provideOrRequestBLEPermission() {
        viewModelScope.launch {
            try {
                controller.providePermission(Permission.BLUETOOTH_SCAN)
                state = PermissionState.Granted
            } catch (e: DeniedAlwaysException) {
                state = PermissionState.DeniedAlways
            } catch (e: DeniedException) {
                state = PermissionState.Denied
            } catch (e: RequestCanceledException) {
                e.printStackTrace()
            }
        }
    }

    fun startScanning() {
        viewModelScope.launch {
            try {
                // Start scanning and collect advertisements
                scanner.advertisements.collect { advertisement ->
                    // You can filter and collect advertisements as needed
                   // _devices.value = _devices.value + advertisement.deviceName
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}