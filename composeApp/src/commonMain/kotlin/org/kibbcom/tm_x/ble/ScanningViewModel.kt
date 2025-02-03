package org.kibbcom.tm_x.ble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juul.kable.Advertisement
import com.juul.kable.Peripheral
import com.juul.kable.Scanner
import com.juul.kable.State

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanningViewModel() : ViewModel(){

    private val scanner = Scanner()

    private var connectedPeripheral: Peripheral? = null

    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList()) // You can store the device names here
    val devices: StateFlow<List<BleDevice>> = _devices



    private val _connectionStatus = MutableStateFlow<State>(State.Disconnected())
    val connectionStatus: StateFlow<State> = _connectionStatus


    fun startScanning() {
        viewModelScope.launch {
            try {

                // Start scanning and collect advertisements
                scanner.advertisements.collect { advertisement ->

                    val deviceName = advertisement.name ?: "Unknown Device"
                    val txPower = advertisement.txPower ?: 0
                    val newDevice = BleDevice(
                        name = deviceName,
                        peripheralName = "",  // Use peripheral name
                        txPower = null,
                        advertisement = advertisement
                    )

                    _devices.update { currentList ->
                        if (newDevice !in currentList) {
                            currentList + newDevice // Add the new device if not already present
                        } else {
                            currentList // Keep the list unchanged if device already exists
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    fun connectToDevice(advertisement: Advertisement?) {
        viewModelScope.launch {
            try {
                val peripheral = advertisement?.let { Peripheral(it) }
                connectedPeripheral = peripheral

                // Observe connection state from Kable
                peripheral?.state?.onEach { state ->
                    _connectionStatus.value = state
                }?.launchIn(viewModelScope)

                peripheral?.connect()

                println("Connecting to ${advertisement?.name ?: "Unknown Device"}")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to connect: ${e.message}")
            }
        }
    }


    fun disconnectDevice() {
        viewModelScope.launch {
            try {
                connectedPeripheral?.disconnect()
                connectedPeripheral = null
                _connectionStatus.value = State.Disconnected()
                println("Device disconnected")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}

data class BleDevice(
    val name: String?,
    val peripheralName: String?,  // Name of the peripheral (or unique identifier)
    val txPower: Int?,
    val advertisement : Advertisement?
)