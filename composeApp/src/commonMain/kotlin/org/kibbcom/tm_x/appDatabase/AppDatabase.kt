package appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase(), DB {

    abstract fun getDao(): GithubRepoDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}


internal const val dbFileName = "tmx_room_db.db"


interface DB {
    fun clearAllTables() {}
}