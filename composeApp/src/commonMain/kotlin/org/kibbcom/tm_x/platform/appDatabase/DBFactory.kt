package appDatabase

import org.kibbcom.tm_x.db.AppDatabase

expect class DBFactory {
    fun createDatabase(): AppDatabase
}