package org.kibbcom.tm_x

import kotlinx.coroutines.flow.StateFlow
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.models.BeaconDevice
import org.kibbcom.tm_x.models.BleDeviceCommon

expect class BleManager() {
    val bleDevicesScanResults: StateFlow<List<BleDeviceCommon>>
    val beaconScanResults: StateFlow<List<BeaconDevice>>
    val connectionState: StateFlow<BleConnectionStatus>
    val readDataResult: StateFlow<Pair<String, ByteArray>?> // 🔥 New state for characteristic read
    fun scanBleDevices()
    fun stopBLEScanning()
    fun scanBeaconDevices()
    fun connectToDevice(deviceId: String)
    fun disConnectToDevice(deviceId: String)
    fun bondWithDevice(deviceId: String)
    fun readBleData(serviceId : String, characteristicUuid : String)
    fun writeBleData(serviceId: String, characteristicId: String, data: ByteArray)
}

