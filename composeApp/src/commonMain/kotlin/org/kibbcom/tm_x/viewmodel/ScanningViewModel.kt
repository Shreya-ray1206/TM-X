package org.kibbcom.tm_x.viewmodel

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.utils.io.charsets.Charsets
import kotlinx.coroutines.delay
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

    private val _readDataResult = MutableStateFlow<Pair<String, ByteArray>?>(null)
    val readDataResult: StateFlow<Pair<String, ByteArray>?> = _readDataResult

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
                        delay(5000)
                        deviceInfo()
                    }
                }
            }
        }
        observeReadData()

        viewModelScope.launch {
            _lastConnectedDevice.value = dao.getLastConnectedDevice()
        }

    }

    private fun observeReadData() {
        viewModelScope.launch {

            bleManager.readDataResult.collectLatest { state ->
                _readDataResult.value = state
                state?.let { (name, data) ->
                    val dataString = data.decodeToString()

                    println("Mukesh Result String: $name, ByteArray size: ${dataString}")
                    _readDataResult.value = state // Update StateFlow if needed
                }

            }
        }
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

    fun deviceInfo(){

        val SERVICE_UUID_DEVICE_INFORMATION = "0000180a-0000-1000-8000-00805f9b34fb";
        val MODEL_NUMBER_STRING = "00002A24-0000-1000-8000-00805f9b34fb"
        val SERIAL_NUMBER_STRING = "00002A25-0000-1000-8000-00805f9b34fb"
        val HARDWARE_REVISION_STRING = "00002A27-0000-1000-8000-00805f9b34fb"
        val FIRMWARE_REVISION_STRING = "00002A26-0000-1000-8000-00805f9b34fb"
        val MANUFACTURER_NAME_STRING = "00002A29-0000-1000-8000-00805f9b34fb"


        readBleData(serviceId = SERVICE_UUID_DEVICE_INFORMATION,
            characteristicUuid = FIRMWARE_REVISION_STRING )
        readBleData(serviceId = SERVICE_UUID_DEVICE_INFORMATION,
            characteristicUuid = MODEL_NUMBER_STRING )
        readBleData(serviceId = SERVICE_UUID_DEVICE_INFORMATION,
            characteristicUuid = HARDWARE_REVISION_STRING )
        readBleData(serviceId = SERVICE_UUID_DEVICE_INFORMATION,
            characteristicUuid = SERIAL_NUMBER_STRING )
        readBleData(serviceId = SERVICE_UUID_DEVICE_INFORMATION,
            characteristicUuid = MANUFACTURER_NAME_STRING )


    }




}

