package org.kibbcom.tm_x.models

data class BeaconDevice(
    val name: String,
    val macAddress: String,
    val rssi: String,
    val major: Int,
    val minor: Int,
    val isSaved :Boolean
)
