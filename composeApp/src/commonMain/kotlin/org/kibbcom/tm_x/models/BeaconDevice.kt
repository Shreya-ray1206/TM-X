package org.kibbcom.tm_x.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "beacon_devices",
)
data class BeaconDevice(
    val name: String,
    val macAddress: String,
    val rssi: String,
    val major: Int,
    val minor: Int,
    val isSaved :Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

)
