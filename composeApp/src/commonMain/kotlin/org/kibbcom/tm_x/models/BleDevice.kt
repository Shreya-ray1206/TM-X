package org.kibbcom.tm_x.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BleDeviceCommon(
    @PrimaryKey val id: String,
    val name : String?
)