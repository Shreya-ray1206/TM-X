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
import org.kibbcom.tm_x.db.AppDatabase
import org.kibbcom.tm_x.models.BleDeviceCommon


@OptIn(ExperimentalStdlibApi::class)
class ScanningViewModel(db: AppDatabase) : ViewModel(){
    private val bleManager = BleManager()
    private val dao = db.getBleDao()
    private var bondingDevice: BleDeviceCommon? = null

    private val _devicesNative = MutableStateFlow<List<BleDeviceCommon>>(emptyList())
    val devicesNative: StateFlow<List<BleDeviceCommon>> = _devicesNative.asStateFlow()

    private val _connectionState = MutableStateFlow<BleConnectionStatus>(BleConnectionStatus.IDLE)
    val connectionState: StateFlow<BleConnectionStatus> = _connectionState.asStateFlow()


    private val _lastConnectedDevice = MutableStateFlow<BleDeviceCommon?>(null)
    val lastConnectedDevice : StateFlow<BleDeviceCommon?> = _lastConnectedDevice.asStateFlow()



    init {
        viewModelScope.launch {
            bleManager.bleDevicesScanResults.collectLatest { scannedDevices ->
                _devicesNative.value = scannedDevices
            }
        }

        viewModelScope.launch {
            bleManager.connectionState.collectLatest { state ->
                _connectionState.value = state
                println("BLE Connection State  $state")
                if (state == BleConnectionStatus.CONNECTED) {
                    println("BLE Connection State Updated to connected $state")
                    bondingDevice?.let {
                        println("BLE Connection State Updated to connected then saved value $state")
                        saveLastConnectedDevice(it)
                        //bondingDevice = null // Reset after saving
                    }
                }


            }
        }

        viewModelScope.launch {
            _lastConnectedDevice.value = dao.getLastConnectedDevice()
        }

        /*
        //todo ios crashing here
        viewModelScope.launch {
            bleManager.readDataResult.collectLatest { state ->
                state?.let { (stringValue, byteArrayValue) ->

                    println("Read Scanning viewmodel ! Data (HEX): ${byteArrayValue.toHexString()}")


                }
            }
        }
*/
    }
    fun scanDevices() {
        bleManager.scanBleDevices()
    }

    fun connectToDevice(deviceId: String) {
        bleManager.connectToDevice(deviceId)
    }

    fun stopScanningDevice() {
        bleManager.stopBLEScanning()
    }

    fun bondWithDevice(device: BleDeviceCommon) {
        bondingDevice = device
        bleManager.bondWithDevice(device.id)
    }

    fun readBleData(serviceId: String, characteristicUuid: String){
        bleManager.readBleData(serviceId,characteristicUuid)
    }

    fun writeBleData(serviceId: String, characteristicId: String, data: ByteArray){
        bleManager.writeBleData(serviceId,characteristicId,data)
    }

    //to save the last connected ble device in db
    fun saveLastConnectedDevice(device: BleDeviceCommon) {
        viewModelScope.launch {
            println("Saved new device $device")
            dao.clearDevice()

            dao.insertOrUpdateDevice(device)
            _lastConnectedDevice.value = dao.getLastConnectedDevice()
        }
    }


}

