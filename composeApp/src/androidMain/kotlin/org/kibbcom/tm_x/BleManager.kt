package org.kibbcom.tm_x

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.room.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.models.BeaconDevice
import org.kibbcom.tm_x.models.BleDeviceCommon
import java.util.UUID

actual class BleManager actual constructor() {


    private val bleReadQueue = Channel<BleReadRequest>(Channel.UNLIMITED)
    private data class BleReadRequest(val serviceId: String, val characteristicUuid: String)

    init {
        processReadQueue()
    }

    @SuppressLint("MissingPermission")
    private fun processReadQueue() {
        CoroutineScope(Dispatchers.IO).launch {
            for (request in bleReadQueue) {
                readCharacteristic(request.serviceId, request.characteristicUuid)
            }
        }
    }


    private val context : Context by lazy {
        AppContextProvider.getContext()
    }

    private val bluetoothAdapter: BluetoothAdapter? =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private var bluetoothGatt: BluetoothGatt? = null
    private val scanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private val _connectionState = MutableStateFlow(BleConnectionStatus.IDLE)
    actual val connectionState = _connectionState.asStateFlow()
    private val _bleDevicesScanResults = MutableStateFlow<List<BleDeviceCommon>>(emptyList())
    actual val bleDevicesScanResults = _bleDevicesScanResults.asStateFlow()

    private val _beaconScanResults = MutableStateFlow<List<BeaconDevice>>(emptyList())
    actual val beaconScanResults = _beaconScanResults.asStateFlow()



    private val _readData = MutableStateFlow<Pair<String, ByteArray>?>(null)
    actual val readDataResult = _readData.asStateFlow()


    private val bondReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == intent?.action) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE)

                when (bondState) {
                    BluetoothDevice.BOND_BONDING -> {
                        _connectionState.value = BleConnectionStatus.BONDING
                        println("Bonding in progress with ${device?.address}...")
                    }

                    BluetoothDevice.BOND_BONDED -> {
                        _connectionState.value = BleConnectionStatus.CONNECTING
                        println("Bonding completed with ${device?.address}, now connecting...")
                        connectToDevice(device?.address ?: return) // Connect after bonding completes
                    }

                    BluetoothDevice.BOND_NONE -> {
                        _connectionState.value = BleConnectionStatus.DISCONNECTED
                        println("Bonding failed or removed for ${device?.address}")
                    }
                }

            }
        }
    }

    init {
        context.registerReceiver(bondReceiver, IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
    }

    @SuppressLint("MissingPermission")
    private val bleScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val newDevice = BleDeviceCommon(
                id = device.address,  // MAC Address
                name = device.name ?: "Unknown"
            )

            if(newDevice.name?.startsWith("Unknown") == false){
                val updatedList = _bleDevicesScanResults.value.toMutableList().apply { add(newDevice) }
                _bleDevicesScanResults.value = updatedList.distinctBy { it.id } // Avoid duplicates
            }

        }
    }


    @SuppressLint("MissingPermission")
    private val beaconScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            println("Beacon Scan result")


            val scanRecord = result.scanRecord ?: return
            val bytes = scanRecord.bytes ?: return

            val iBeacon = parseIBeacon(scanRecord)
            val eddystone = parseEddystone(bytes)
            println("Beacon Scan result $iBeacon")
            println("Beacon Scan result $eddystone")

            if (scanRecord != null) {
                val iBeaconManufactureData = scanRecord.getManufacturerSpecificData(0x004C)
                if (iBeaconManufactureData != null && iBeaconManufactureData.size >= 23) {


                    val length = iBeaconManufactureData.size
                    val companyId = 0x004C
                    val type = iBeaconManufactureData[0].toInt()
                    val uuidBytes = iBeaconManufactureData.copyOfRange(2, 18)
                    val iBeaconUUID = convertToUUIDString(uuidBytes)
                    val major = (iBeaconManufactureData[18].toInt() and 0xFF) shl 8 or (iBeaconManufactureData[19].toInt() and 0xFF)
                    val minor = (iBeaconManufactureData[20].toInt() and 0xFF) shl 8 or (iBeaconManufactureData[21].toInt() and 0xFF)
                    // Extract TX power level (calibrated RSSI at 1 meter)
                    val txPowerCalibratedRSSI = iBeaconManufactureData[22].toInt()


                    val beaconDevice =  BeaconDevice("iBeacon",
                        result.device.address,txPowerCalibratedRSSI.toString(),iBeaconUUID,
                        major,minor,length,companyId =companyId.toString())

                    val updatedList = _beaconScanResults.value.toMutableList().apply { add(beaconDevice) }
                    _beaconScanResults.value = updatedList.distinctBy { it.macAddress }

                }
            }




            _beaconScanResults.value = _beaconScanResults.value.distinctBy { it.macAddress }


        }
    }

    private fun convertToUUIDString(uuidBytes: ByteArray): String {
        val hexString = uuidBytes.joinToString("") { String.format("%02X", it) }
        return String.format(
            "%s-%s-%s-%s-%s",
            hexString.substring(0, 8),
            hexString.substring(8, 12),
            hexString.substring(12, 16),
            hexString.substring(16, 20),
            hexString.substring(20, 32)
        )
    }


    data class IBeaconData(val uuid: String, val major: Int, val minor: Int, val txPower: Int)

    private fun parseIBeacon(scanRecord: ScanRecord): IBeaconData? {
        val manufacturerData = scanRecord.getManufacturerSpecificData(0x004C) ?: return null
        if (manufacturerData.size < 23) return null

        val uuidBytes = manufacturerData.copyOfRange(2, 18)
        val uuid = convertToUUIDString(uuidBytes)
        val major = (manufacturerData[18].toInt() and 0xFF) shl 8 or (manufacturerData[19].toInt() and 0xFF)
        val minor = (manufacturerData[20].toInt() and 0xFF) shl 8 or (manufacturerData[21].toInt() and 0xFF)
        val txPower = manufacturerData[22].toInt()

        return IBeaconData(uuid, major, minor, txPower)
    }


    data class EddystoneData(val namespace: String, val instanceId: String)

    private fun parseEddystone(bytes: ByteArray): EddystoneData? {
        if (bytes.size < 20) return null
        if (bytes[0] != 0x00.toByte() || bytes[1] != 0x00.toByte()) return null  // Eddystone UID frame check

        val namespace = bytes.copyOfRange(2, 12).joinToString("") { "%02x".format(it) }
        val instanceId = bytes.copyOfRange(12, 18).joinToString("") { "%02x".format(it) }

        return EddystoneData(namespace, instanceId)
    }






    @SuppressLint("MissingPermission")
    actual fun scanBleDevices() {
        scanner?.startScan(bleScanCallback)
        println("BLE scanning started...")
        _connectionState.value = BleConnectionStatus.SCANNING

        Handler(Looper.getMainLooper()).postDelayed({
            scanner?.stopScan(bleScanCallback)
            println("BLE scanning stopped")
        }, 100000) // Stop scanning after 10 seconds
    }

   @SuppressLint("MissingPermission")
    actual fun scanBeaconDevices() {
        scanner?.stopScan(bleScanCallback)
        scanner?.startScan(beaconScanCallback)
        println("Beacon  scanning started...")

        Handler(Looper.getMainLooper()).postDelayed({
            scanner?.stopScan(beaconScanCallback)
            println("BLE scanning stopped")
        }, 100000) // Stop scanning after 10 seconds
    }




    @OptIn(ExperimentalStdlibApi::class)
    @SuppressLint("MissingPermission")
    actual fun connectToDevice(deviceId: String) {
        val device = bluetoothAdapter?.getRemoteDevice(deviceId)
        if (device == null) {
            println("Device not found: $deviceId")
            return
        }

        _connectionState.value = BleConnectionStatus.CONNECTING

        device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        println("Connected to $deviceId, discovering services...")
                        _connectionState.value = BleConnectionStatus.CONNECTED
                        bluetoothGatt = gatt
                        gatt.discoverServices()
                    }

                    BluetoothProfile.STATE_DISCONNECTED -> {
                        println("Disconnected from $deviceId")
                        _connectionState.value = BleConnectionStatus.DISCONNECTED
                        bluetoothGatt?.close()
                    }
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    println("Services discovered successfully!")

                    // 🔥 Print all available services and characteristics
                    gatt.services.forEach { service ->
                        println("Service: ${service.uuid}")

                        service.characteristics.forEach { characteristic ->
                            println(" - Characteristic: ${characteristic.uuid}")
                        }
                    }

                } else {
                    println("Failed to discover services, status: $status")
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS && characteristic != null) {
                    val data = characteristic.value
                    val characteristicUuid = characteristic.uuid
                    _readData.value = characteristicUuid.toString() to data
                    println("Read characteristic success! Data (HEX): ${data.toHexString()}")
                    val dataString = data.toString(Charsets.UTF_8)
                    println("Received Data as String: $dataString")
                } else {
                    println("Failed to read characteristic, status: $status")
                }
            }
        })
    }





    @SuppressLint("MissingPermission")
    actual fun bondWithDevice(deviceId: String) {
        println("Bond with device method called ")
        val device = bluetoothAdapter?.getRemoteDevice(deviceId)
        if (device == null) {
            println("Device not found: $deviceId")
            return
        }

        when (device.bondState) {
            BluetoothDevice.BOND_BONDED -> {
                println("Device is already bonded: $deviceId, proceeding with connection...")
                _connectionState.value = BleConnectionStatus.CONNECTING
                connectToDevice(deviceId) // Directly connect if already bonded
            }
            BluetoothDevice.BOND_NONE -> {
                _connectionState.value = BleConnectionStatus.BONDING
                println("Bonding initiated with $deviceId")
                device.createBond()
            }
            BluetoothDevice.BOND_BONDING -> {
                println("Device is currently bonding: $deviceId")
                _connectionState.value = BleConnectionStatus.BONDING
            }
        }
    }


    @SuppressLint("MissingPermission")
    actual fun stopBLEScanning() {
        scanner?.stopScan(bleScanCallback)
    }

    @SuppressLint("MissingPermission")
    actual fun disConnectToDevice(deviceId: String) {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        _connectionState.value = BleConnectionStatus.DISCONNECTED
    }


    @SuppressLint("MissingPermission")
    actual fun readBleData(serviceId: String, characteristicUuid: String) {
        bleReadQueue.trySend(BleReadRequest(serviceId, characteristicUuid))
    }

    @SuppressLint("MissingPermission")
    actual fun writeBleData(serviceId: String, characteristicId: String, data: ByteArray) {
        val gatt = bluetoothGatt
        if (gatt == null) {
            println("BluetoothGatt is null, cannot write characteristic.")
            return
        }

        val service = gatt.getService(UUID.fromString(serviceId))
        if (service == null) {
            println("Service with UUID $serviceId not found.")
            return
        }

        val characteristic = service.getCharacteristic(UUID.fromString(characteristicId))
        if (characteristic == null) {
            println("Characteristic with UUID $characteristicId not found.")
            return
        }

        // Check if the characteristic supports writing
        val writeProperty = characteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE
        val writeNoResponseProperty = characteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE

        if (writeProperty == 0 && writeNoResponseProperty == 0) {
            println("Characteristic does not support writing.")
            return
        }

        characteristic.value = data

        val success = gatt.writeCharacteristic(characteristic)
        println("Write characteristic request sent: $success")
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    suspend fun readCharacteristic(serviceId: String, characteristicUuid: String){
        val serviceUuid = UUID.fromString(serviceId)
        val characteristicUuids = UUID.fromString(characteristicUuid)

        val gatt = bluetoothGatt
        if (gatt == null) {
            println("BluetoothGatt is null, cannot read characteristic.")
            return
        }

        val service = gatt.getService(serviceUuid) ?: run {
            println("Service with UUID $serviceUuid not found.")
            return
        }

        val characteristic = service.getCharacteristic(characteristicUuids) ?: run {
            println("Characteristic with UUID $characteristicUuids not found.")
            return
        }

        if (gatt.readCharacteristic(characteristic)) {
            println("Read characteristic request sent successfully.")
            waitForReadCompletion(characteristicUuids.toString())
        } else {
            println("Failed to initiate read request.")
        }
    }

    private suspend fun waitForReadCompletion(expectedUuid: String) {
        readDataResult.first { it?.first == expectedUuid }
        delay(500) // Ensure slight delay to prevent rapid consecutive reads
    }
}