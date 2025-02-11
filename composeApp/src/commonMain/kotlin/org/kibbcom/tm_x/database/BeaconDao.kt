package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.kibbcom.tm_x.models.BeaconDevice

@Dao
interface BeaconDao {

    @Upsert
    suspend fun upsert(person: BeaconDevice)

    @Delete
    suspend fun delete(person: BeaconDevice)

    @Query("SELECT * FROM beacon_devices")
    fun getAllPeople(): Flow<List<BeaconDevice>>

}