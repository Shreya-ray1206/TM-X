package database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.kibbcom.tm_x.models.BeaconDevice

@Database(
    entities = [BeaconDevice::class],
    version = 1
)
abstract class TmxDatabase: RoomDatabase() {

    abstract fun beaconDao(): BeaconDao

}