package org.kibbcom.tm_x.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kibbcom.tm_x.BleManager
import org.kibbcom.tm_x.models.BeaconDevice

class BeaconViewModel() : ViewModel() {
    private val bleManager = BleManager()
    private val _beaconDevices = MutableStateFlow<List<BeaconDevice>>(emptyList())
    val devicesNative: StateFlow<List<BeaconDevice>> = _beaconDevices.asStateFlow()

    init {
        viewModelScope.launch {
            bleManager.beaconScanResults.collectLatest { scannedDevices ->
                _beaconDevices.value = scannedDevices
            }
        }

    }

    fun scanBeaconDevices() {
        bleManager.scanBeaconDevices()
    }


}
