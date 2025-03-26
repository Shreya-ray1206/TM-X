package org.kibbcom.tm_x.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class BeaconDevice(
    val name: String,  // "iBeacon" or "Eddystone"
    @PrimaryKey val macAddress: String,
    val rssi: String,
    val uuid: String? = null,
    val major: Int? = null,
    val minor: Int? = null,
    val length: Int? = null,
    val companyId: String? = null,

)

