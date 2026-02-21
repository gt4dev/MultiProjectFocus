package gtr.mpfocus.infra.db_repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: ProjectEntity): Long

    @Query("DELETE FROM projects")
    suspend fun deleteAll()

    @Query("SELECT * FROM projects WHERE isCurrent = 1 LIMIT 1")
    fun getCurrentProject(): Flow<ProjectEntity?>

    @Query("UPDATE projects SET isCurrent = false")
    suspend fun clearCurrentProject()

    @Query("UPDATE projects SET isCurrent = 1 WHERE id = :projectId")
    suspend fun setCurrentProject(projectId: Long)

    @Query("SELECT * FROM projects WHERE pinPosition IS NOT NULL ORDER BY pinPosition ASC")
    fun getPinnedProjects(): Flow<List<ProjectEntity>>

    @Query("UPDATE projects SET pinPosition = NULL")
    suspend fun clearAllPins()

    @Query("UPDATE projects SET pinPosition = :pinPosition WHERE id = :projectId")
    suspend fun pinProject(projectId: Long, pinPosition: Int)

    @Query("SELECT * FROM projects ORDER BY id ASC")
    fun getAll(): Flow<List<ProjectEntity>>
}