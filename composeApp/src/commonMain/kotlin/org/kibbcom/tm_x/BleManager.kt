package org.kibbcom.tm_x

import kotlinx.coroutines.flow.StateFlow
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.ble.BleDeviceCommon

expect class BleManager() {
    val scanResults: StateFlow<List<BleDeviceCommon>>
    val connectionState: StateFlow<BleConnectionStatus> // Connection state flow
    fun scanDevices()
    fun stopScanning()
    fun connectToDevice(deviceId: String)
    fun disConnectToDevice(deviceId: String)
    fun bondWithDevice(deviceId: String)
}
