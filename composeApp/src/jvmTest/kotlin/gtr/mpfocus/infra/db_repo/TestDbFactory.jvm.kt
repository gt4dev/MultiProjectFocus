package gtr.mpfocus.infra.db_repo

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

internal actual fun createInMemoryTestDb(): MPFDatabase {
    return Room.inMemoryDatabaseBuilder<MPFDatabase>()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
