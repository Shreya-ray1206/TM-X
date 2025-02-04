package org.kibbcom.tm_x

import com.juul.kable.Advertisement
import kotlinx.coroutines.flow.StateFlow
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.ble.BleDeviceCommon

actual class BleManager actual constructor() {
    actual fun scanDevices() {
    }

    actual fun connectToDevice(deviceId: String) {
    }

    actual fun bondWithDevice(deviceId: String) {
    }

    actual val scanResults: StateFlow<List<BleDeviceCommon>>
        get() = TODO("Not yet implemented")
    actual val connectionState: StateFlow<BleConnectionStatus>
        get() = TODO("Not yet implemented")

    actual fun stopScanning() {
    }

    actual fun disConnectToDevice(deviceId: String) {
    }


}