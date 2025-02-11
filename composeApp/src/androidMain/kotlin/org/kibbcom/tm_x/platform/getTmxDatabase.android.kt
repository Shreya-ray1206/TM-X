package org.kibbcom.tm_x.platform

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.BeaconDatabase
import org.kibbcom.tm_x.AppContextProvider

actual fun getTmxDatabase() : BeaconDatabase {
     val context : Context by lazy {
        AppContextProvider.getContext()
    }

    val dbFile = context.getDatabasePath("tmx.db")
    return Room.databaseBuilder<BeaconDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}