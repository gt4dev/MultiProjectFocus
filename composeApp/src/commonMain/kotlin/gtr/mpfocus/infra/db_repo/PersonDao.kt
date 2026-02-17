package gtr.mpfocus.infra.db_repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: PersonEntity): Long

    @Query("SELECT * FROM people ORDER BY id ASC")
    fun getAll(): Flow<List<PersonEntity>>
}
