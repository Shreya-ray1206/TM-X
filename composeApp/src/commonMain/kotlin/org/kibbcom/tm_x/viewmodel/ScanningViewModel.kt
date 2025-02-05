package org.kibbcom.tm_x.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kibbcom.tm_x.BleManager
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.ble.BleDeviceCommon


@OptIn(ExperimentalStdlibApi::class)
class ScanningViewModel() : ViewModel(){
    private val bleManager = BleManager()


    private val _devicesNative = MutableStateFlow<List<BleDeviceCommon>>(emptyList())
    val devicesNative: StateFlow<List<BleDeviceCommon>> = _devicesNative.asStateFlow()

    private val _connectionState = MutableStateFlow<BleConnectionStatus>(BleConnectionStatus.IDLE)
    val connectionState: StateFlow<BleConnectionStatus> = _connectionState.asStateFlow()

    init {
        viewModelScope.launch {
            bleManager.scanResults.collectLatest { scannedDevices ->
                _devicesNative.value = scannedDevices
            }
        }

        viewModelScope.launch {
            bleManager.connectionState.collectLatest { state ->
                _connectionState.value = state
                println("BLE Connection State Updated: $state")
            }
        }
        viewModelScope.launch {
            bleManager.readDataResult.collectLatest { state ->
                state?.let { (stringValue, byteArrayValue) ->

                    println("Read Scanning viewmodel ! Data (HEX): ${byteArrayValue.toHexString()}")


                }
            }
        }

    }
    fun scanDevices() {
        bleManager.scanDevices()
    }

    fun connectToDevice(deviceId: String) {
        bleManager.connectToDevice(deviceId)
    }

    fun stopScanningDevice() {
        bleManager.stopScanning()
    }

    fun bondWithDevice(deviceId: String) {
        bleManager.bondWithDevice(deviceId)
    }

    fun readBleData(serviceId: String, characteristicUuid: String){
        bleManager.readBleData(serviceId,characteristicUuid)
    }


}

