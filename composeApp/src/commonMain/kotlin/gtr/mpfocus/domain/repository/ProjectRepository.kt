package gtr.mpfocus.domain.repository

import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun deleteProject(projectId: Long)
    suspend fun deleteAll()

    fun getCurrentProject(): Flow<Project?>
    suspend fun setCurrentProject(projectId: Long?)

    fun getPinnedProjects(): Flow<List<Project>>
    suspend fun clearAllPins()
    suspend fun pinProject(projectId: Long, pinPosition: Int)

    fun getOtherProjects(): Flow<List<Project>>

    suspend fun addProject(projectPath: FolderPath): Long
    suspend fun getProject(projectId: Long): Project?
    fun getAll(): Flow<List<Project>>
}
