package org.kibbcom.tm_x
//
//import androidx.lifecycle.ViewModel
//import com.juul.kable.Scanner
//
//
//
//
//import androidx.compose.runtime.State
//
//
//import androidx.lifecycle.viewModelScope
//
//import com.juul.kable.Peripheral
//
//
//import kotlinx.coroutines.flow.MutableStateFlow
//
//import kotlinx.coroutines.flow.StateFlow
//
//import kotlinx.coroutines.flow.update
//
//import kotlinx.coroutines.launch
//
//
//class ScanningViewModel : ViewModel(){
//
//    private val scanner = Scanner()
//
//
//    private val _devices = MutableStateFlow<List<String>>(emptyList()) // You can store the device names here
//    val devices: StateFlow<List<String>> = _devices
//
//      // State flow for connection state
//       private val _connectionState = MutableStateFlow<Peripheral.State>(Peripheral.State.Disconnected)
//       val connectionState: StateFlow<Peripheral.State> = _connectionState
//
//
//    fun startScanning() {
//        viewModelScope.launch {
//            try {
//
//               scanner.p
//
//                // Start scanning and collect advertisements
//                scanner.advertisements.collect { advertisement ->
//
//                    val deviceName = advertisement.name ?: "Unknown Device"
//                    val txPower = advertisement.txPower ?: 0
//
//
//
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//
//
//
//}