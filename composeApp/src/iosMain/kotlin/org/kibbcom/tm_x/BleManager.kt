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
            // Check if the peripheral is already connected
            if (it.state != platform.CoreBluetooth.CBPeripheralStateConnected) {
                centralManager?.connectPeripheral(it, null)
                println("Connecting to $deviceId")
            } else {
                println("Already connected to $deviceId")
            }
        } ?: println("Device not found")
    }

    actual fun bondWithDevice(deviceId: String) {
        val peripheral = discoveredPeripherals[deviceId]
        peripheral?.let {
            // Check if the peripheral is connected
            if (it.state == platform.CoreBluetooth.CBPeripheralStateConnected) {
                it.delegate = this
                _connectionState.value = BleConnectionStatus.BONDING
                println("Bonding initiated with $deviceId")
                it.discoverServices(null) // This triggers `didDiscoverServices`
            } else {
                // If not connected, connect first
                _connectionState.value = BleConnectionStatus.CONNECTING
                centralManager?.connectPeripheral(it, null)
                println("Connecting to $deviceId")
            }
        } ?: println("Device not found")
    }

    override fun centralManagerDidUpdateState(central: CBCentralManager) {
        if (central.state == CBManagerStatePoweredOn) {
            println("Bluetooth is enabled.")
            // Initiate scanning only when Bluetooth is enabled
            centralManager?.scanForPeripheralsWithServices(null, null)
        } else {
            println("Bluetooth is not available.")
        }
    }

    override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
        val id = didDiscoverPeripheral.identifier.UUIDString
        println("Discovered peripheral: $id")

        // Ensure peripheral is not already in the list
        if (!discoveredPeripherals.containsKey(id)) {
            discoveredPeripherals[id] = didDiscoverPeripheral
            val newDevice = BleDeviceCommon(id = id, name = didDiscoverPeripheral.name ?: "Unknown")

            if(newDevice.name?.startsWith("Unknown") == false){
                val updatedList = _scanResults.value.toMutableList().apply { add(newDevice) }
                _scanResults.value = updatedList.distinctBy { it.id }
                println("Discovered device: ${newDevice.name} ($id)")
            }
        }
    }

    override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
        val deviceId = didConnectPeripheral.identifier.UUIDString
        if (_connectionState.value != BleConnectionStatus.CONNECTED) {
            println("Connected to $deviceId")
            _connectionState.value = BleConnectionStatus.CONNECTED
            didConnectPeripheral.delegate = this
            didConnectPeripheral.discoverServices(null)
        } else {
            println("Already connected to $deviceId. Skipping service discovery.")
        }
    }

    // Called when services are discovered
    override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
        val deviceId = peripheral.identifier.UUIDString
        if (didDiscoverServices == null) {
            println("Services discovered for $deviceId")

            // Only proceed if the connection state is CONNECTED
            if (_connectionState.value == BleConnectionStatus.CONNECTED) {
                // Proceed with further bonding or service interactions
                _connectionState.value = BleConnectionStatus.CONNECTED
            } else {
                println("Service discovery failed because the peripheral is not connected.")
                _connectionState.value = BleConnectionStatus.DISCONNECTED
            }
        } else {
            println("Service discovery failed: ${didDiscoverServices.localizedDescription}")
            _connectionState.value = BleConnectionStatus.DISCONNECTED
        }
    }

    actual fun stopScanning() {
        centralManager?.stopScan()
    }

    actual fun disConnectToDevice(deviceId: String) {
        val peripheral = discoveredPeripherals[deviceId]
        peripheral?.let {
            centralManager?.cancelPeripheralConnection(it)
            _connectionState.value = BleConnectionStatus.DISCONNECTED
            println("Disconnected from $deviceId")
        } ?: println("Device not found")
    }
}
