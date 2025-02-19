package org.kibbcom.tm_x.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.kibbcom.tm_x.models.BleDeviceCommon

@Dao
interface BleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replaces old data with new data
    suspend fun insertOrUpdateDevice(device: BleDeviceCommon)

    @Query("SELECT * FROM BleDeviceCommon LIMIT 1")
    suspend fun getLastConnectedDevice(): BleDeviceCommon?

    @Query("DELETE FROM BleDeviceCommon")
    suspend fun clearDevice()

}