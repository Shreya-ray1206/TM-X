package org.kibbcom.tm_x.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BeaconDevice(
    val name: String,
    @PrimaryKey val macAddress: String,
    val rssi: String,
    val major: Int,
    val minor: Int,
    val isSaved :Boolean
)
