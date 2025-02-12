package org.kibbcom.tm_x.platform

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.TmxDatabase
import platform.Foundation.NSHomeDirectory


actual fun getTmxDatabase(): TmxDatabase {
    val dbFile = NSHomeDirectory() + "/tmx.db"
    return Room.databaseBuilder<TmxDatabase>(
        name = dbFile,
        factory = { TmxDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}