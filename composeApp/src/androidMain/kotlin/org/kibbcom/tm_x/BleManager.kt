package org.kibbcom.tm_x

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
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.ble.BleDeviceCommon
import java.util.UUID

actual class BleManager actual constructor() {

    private val context : Context by lazy {
        AppContextProvider.getContext()
    }

    private val bluetoothAdapter: BluetoothAdapter? =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private var bluetoothGatt: BluetoothGatt? = null
    private val scanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private val _connectionState = MutableStateFlow(BleConnectionStatus.IDLE)
    actual val connectionState = _connectionState.asStateFlow()
    private val _scanResults = MutableStateFlow<List<BleDeviceCommon>>(emptyList())
    actual val scanResults = _scanResults.asStateFlow()
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
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val newDevice = BleDeviceCommon(
                id = device.address,  // MAC Address
                name = device.name ?: "Unknown"
            )

            if(newDevice.name?.startsWith("Unknown") == false){
                val updatedList = _scanResults.value.toMutableList().apply { add(newDevice) }
                _scanResults.value = updatedList.distinctBy { it.id } // Avoid duplicates
            }

        }
    }


    @SuppressLint("MissingPermission")
    actual fun scanDevices() {
        scanner?.startScan(scanCallback)
        println("BLE scanning started...")
        _connectionState.value = BleConnectionStatus.SCANNING

        Handler(Looper.getMainLooper()).postDelayed({
            scanner?.stopScan(scanCallback)
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

                   /* // 🔥 UUIDs for service and characteristic
                    val serviceUuid = UUID.fromString("EC7B0001-EDFF-4CCE-9CF8-3B175487D710")
                    val characteristicUuid = UUID.fromString("EC7B0004-EDFF-4CCE-9CF8-3B175487D710")

                    readCharacteristic(serviceUuid, characteristicUuid)*/
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
    actual fun stopScanning() {
        scanner?.stopScan(scanCallback)
    }

    actual fun disConnectToDevice(deviceId: String) {
    }


    @SuppressLint("MissingPermission")
    actual fun readBleData(serviceId: String, characteristicUuid: String) {
        val serviceUuid = UUID.fromString(serviceId)
        val characteristicUuids = UUID.fromString(characteristicUuid)


        println("BluetoothGatt Read method is called.")

        val gatt = bluetoothGatt
        if (gatt == null) {
            println("BluetoothGatt is null, cannot read characteristic.")
            return
        }

        val service = gatt.getService(serviceUuid)
        if (service == null) {
            println("Service with UUID $serviceUuid not found.")
            return
        }

        val characteristic = service.getCharacteristic(characteristicUuids)
        if (characteristic == null) {
            println("Characteristic with UUID $characteristicUuids not found.")
            return
        }

        val success = gatt.readCharacteristic(characteristic)
        println("Read characteristic request sent: $success")

    }


}