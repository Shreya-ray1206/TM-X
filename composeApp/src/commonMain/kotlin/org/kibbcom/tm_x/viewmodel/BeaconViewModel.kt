package org.kibbcom.tm_x.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kibbcom.tm_x.BleManager
import org.kibbcom.tm_x.db.AppDatabase
import org.kibbcom.tm_x.models.BeaconDevice

class BeaconViewModel(private val db: AppDatabase) : ViewModel() {
    private val bleManager = BleManager()

    private val _nearbyBeaconDevices = MutableStateFlow<List<BeaconDevice>>(emptyList())
    val nearbyBeaconDevices: StateFlow<List<BeaconDevice>> = _nearbyBeaconDevices.asStateFlow()

    private val _savedBeacons = MutableStateFlow<List<BeaconDevice>>(emptyList())
    val savedBeacons: StateFlow<List<BeaconDevice>> = _savedBeacons.asStateFlow()




   init {
       //todo ios crashing here
       viewModelScope.launch {
            bleManager.beaconScanResults.collectLatest { scannedDevices ->
                _nearbyBeaconDevices.value = scannedDevices
            }
        }

        // Load saved beacons from the database when ViewModel initializes
        viewModelScope.launch {
            _savedBeacons.value = db.getBeaconDao().getAll()
        }
    }


    fun scanBeaconDevices() {
        bleManager.scanBeaconDevices()
    }

    fun saveBeacon(device: BeaconDevice) {
        viewModelScope.launch {
            db.getBeaconDao().insert(device)
            // Refresh saved beacons list
            _savedBeacons.value = db.getBeaconDao().getAll()
        }
    }
    fun removeBeacon(device: BeaconDevice) {
        viewModelScope.launch {
            db.getBeaconDao().deleteByMacAddress(device.macAddress)
            // Refresh saved beacons list
            _savedBeacons.value = db.getBeaconDao().getAll()
        }
    }


}

