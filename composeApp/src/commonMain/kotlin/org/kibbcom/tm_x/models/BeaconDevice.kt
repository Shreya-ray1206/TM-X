package org.kibbcom.tm_x.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class BeaconDevice(
    val name: String,  // "iBeacon" or "Eddystone"
    @PrimaryKey val macAddress: String,  // Unique identifier
    val rssi: String,  // Signal strength
    val uuid: String? = null,  // iBeacon UUID (nullable for Eddystone)
    val major: Int? = null,  // iBeacon major (nullable for Eddystone)
    val minor: Int? = null,  // iBeacon minor (nullable for Eddystone)
    val namespace: String? = null,  // Eddystone namespace (nullable for iBeacon)
    val instanceId: String? = null  // Eddystone instance ID (nullable for iBeacon)
)

