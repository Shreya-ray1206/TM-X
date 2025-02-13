package appDatabase

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.kibbcom.tm_x.db.AppDatabase
import org.kibbcom.tm_x.db.dbFileName
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DBFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDatabase(): AppDatabase {
        val docDirectoryUrl = NSFileManager.defaultManager().URLForDirectory(NSDocumentDirectory, NSUserDomainMask, null, true, null)
        val dbFile = docDirectoryUrl?.path?.let {
            if (it.endsWith("/")) "$it$dbFileName" else "$it/$dbFileName"
        } ?: throw IllegalStateException("Documents directory not found")

        return Room.databaseBuilder<AppDatabase>(dbFile,
            factory = {
                AppDatabase::class.instantiateImpl()
            })
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}