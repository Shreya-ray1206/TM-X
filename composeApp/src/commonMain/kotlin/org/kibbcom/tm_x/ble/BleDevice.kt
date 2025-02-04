package org.kibbcom.tm_x.ble

data class BleDeviceCommon(
    val id : String, // MAC address (Android) / UUID (iOS)
    val name : String?
)