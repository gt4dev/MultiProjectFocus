package gtr.mpfocus.infra.db_repo

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [PersonEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(MPFDatabaseConstructor::class)
abstract class MPFDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao // todo: create ProjectDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object MPFDatabaseConstructor : RoomDatabaseConstructor<MPFDatabase> {
    override fun initialize(): MPFDatabase
}

expect fun getMPFDatabaseBuilder(): RoomDatabase.Builder<MPFDatabase>

fun createMPFDatabase(): MPFDatabase {
    return createMPFDatabase(getMPFDatabaseBuilder())
}

private fun createMPFDatabase(
    builder: RoomDatabase.Builder<MPFDatabase>
): MPFDatabase = builder
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()

internal fun mpfDatabaseFileName(): String = "multi_project_focus.db"
