package org.kibbcom.tm_x.platform

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.TmxDatabase
import org.kibbcom.tm_x.AppContextProvider

actual fun getTmxDatabase() : TmxDatabase {
     val context : Context by lazy {
        AppContextProvider.getContext()
    }

    val dbFile = context.getDatabasePath("tmx.db")
    return Room.databaseBuilder<TmxDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}