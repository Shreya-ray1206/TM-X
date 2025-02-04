package org.kibbcom.tm_x

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kibbcom.tm_x.ble.BleConnectionStatus
import org.kibbcom.tm_x.ble.BleDeviceCommon
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

actual class BleManager actual constructor() : NSObject(), CBCentralManagerDelegateProtocol,
    CBPeripheralDelegateProtocol {

    private var centralManager: CBCentralManager? = null
    private var discoveredPeripherals = mutableMapOf<String, CBPeripheral>()
    private val _scanResults = MutableStateFlow<List<BleDeviceCommon>>(emptyList())
    actual val scanResults: StateFlow<List<BleDeviceCommon>> = _scanResults.asStateFlow()
    private val _connectionState = MutableStateFlow(BleConnectionStatus.IDLE)
    actual val connectionState = _connectionState.asStateFlow()
    init {
        centralManager = CBCentralManager(this, null)
    }

    actual fun scanDevices() {
        if (centralManager?.state == CBManagerStatePoweredOn) {
            centralManager?.scanForPeripheralsWithServices(null, null)
            println("BLE scanning started...")
        } else {
            println("Bluetooth is not enabled.")
        }

    }

    actual fun connectToDevice(deviceId: String) {
        val peripheral = discoveredPeripherals[deviceId]
        peripheral?.let {
            centralManager?.connectPeripheral(it, null)
            println("Connecting to $deviceId")
        } ?: println("Device not found")

    }


    actual fun bondWithDevice(deviceId: String) {
        val peripheral = discoveredPeripherals[deviceId]
        peripheral?.let {
            it.delegate = this
            _connectionState.value = BleConnectionStatus.BONDING
            println("Bonding initiated with $deviceId")
            it.discoverServices(null) // This triggers `didDiscoverServices`
        } ?: println("Device not found")
    }

    override fun centralManagerDidUpdateState(central: CBCentralManager) {
        if (central.state == CBManagerStatePoweredOn) {
            println("Bluetooth is enabled.")
        } else {
            println("Bluetooth is not available.")
        }
    }

    override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
        val id = didDiscoverPeripheral.identifier.UUIDString
        if (!discoveredPeripherals.containsKey(id)) {
            discoveredPeripherals[id] = didDiscoverPeripheral
            val newDevice = BleDeviceCommon(id = id, name = didDiscoverPeripheral.name ?: "Unknown")
            val updatedList = _scanResults.value.toMutableList().apply { add(newDevice) }
            _scanResults.value = updatedList.distinctBy { it.id }
            println("Discovered device: ${newDevice.name} ($id)")
        }
    }

    override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
        println("Connected to ${didConnectPeripheral.identifier.UUIDString}")
        didConnectPeripheral.delegate = this
        didConnectPeripheral.discoverServices(null)
    }

    // Called when services are discovered
    override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
        if (didDiscoverServices == null) {
            println("Services discovered for ${peripheral.identifier.UUIDString}")

            // Bonding complete, now connect
            val deviceId = peripheral.identifier.UUIDString
            _connectionState.value = BleConnectionStatus.CONNECTING
            connectToDevice(deviceId)
        } else {
            println("Service discovery failed: ${didDiscoverServices.localizedDescription}")
            _connectionState.value = BleConnectionStatus.DISCONNECTED
        }
    }

    actual fun stopScanning() {
        centralManager?.stopScan()
    }

    actual fun disConnectToDevice(deviceId: String) {
    }


}