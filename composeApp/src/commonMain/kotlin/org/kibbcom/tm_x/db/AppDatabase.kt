package org.kibbcom.tm_x.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.kibbcom.tm_x.models.BeaconDevice

@Database(entities = [BeaconDevice::class], version =  1)
abstract class AppDatabase: RoomDatabase(), DB {
    abstract fun getBeaconDao(): BeaconDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}


internal const val dbFileName = "tmx_room_db.db"


interface DB {
    fun clearAllTables() {}
}