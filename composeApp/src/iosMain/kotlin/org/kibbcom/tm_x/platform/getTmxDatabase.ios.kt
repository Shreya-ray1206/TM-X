package org.kibbcom.tm_x.platform

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.BeaconDatabase
import platform.Foundation.NSHomeDirectory


actual fun getTmxDatabase(): BeaconDatabase {
    val dbFile = NSHomeDirectory() + "/tmx.db"
    return Room.databaseBuilder<BeaconDatabase>(
        name = dbFile,
        factory = { BeaconDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}