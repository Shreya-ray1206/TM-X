package org.kibbcom.tm_x.models

data class BleDeviceCommon(
    val id : String, // MAC address (Android) / UUID (iOS)
    val name : String?
)