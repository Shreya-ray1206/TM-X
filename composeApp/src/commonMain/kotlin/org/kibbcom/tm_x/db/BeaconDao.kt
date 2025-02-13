package org.kibbcom.tm_x.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.kibbcom.tm_x.models.BeaconDevice


@Dao
interface BeaconDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BeaconDevice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<BeaconDevice>)

    @Query("SELECT * FROM BeaconDevice")
    fun getAllAsFlow(): Flow<List<BeaconDevice>>


    @Query("SELECT * FROM BeaconDevice")
    suspend fun getAll(): List<BeaconDevice>

    @Query("SELECT count(*) FROM BeaconDevice")
    suspend fun count(): Int

}

