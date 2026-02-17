package gtr.mpfocus.infra.db_repo

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getMPFDatabaseBuilder(): RoomDatabase.Builder<MPFDatabase> {
    val dbFile = File(System.getProperty("user.home"), ".mpfocus/" + mpfDatabaseFileName())
    dbFile.parentFile?.mkdirs()
    return Room.databaseBuilder<MPFDatabase>(name = dbFile.absolutePath)
}
